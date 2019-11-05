#include <netinet/in.h>
#include <sys/socket.h>
#include <unistd.h>
#include <errno.h>
#include <stdlib.h>
#include <string.h>
#include <stdio.h>
#include <inttypes.h>
#include <strings.h>
#include <pthread.h>

#include "./factor.h"

#define PORT 8080

const char RESPONSE_400[] = "HTTP/1.1 400 Bad Request\nContent-Type: application/json\nConnection: close\nContent-Length: 2\n\n[]";
const size_t RESPONSE_400_LENGTH = sizeof(RESPONSE_400)-1; //Skip trailing 0 byte!

const char RESPONSE_200_PRELUDE[] = "HTTP/1.1 200 OK\nContent-Type: application/json; Connection: close\n";
const size_t RESPONSE_200_PRELUDE_LENGTH = sizeof(RESPONSE_200_PRELUDE)-1; //Skip trailing 0 byte!

const char REQUEST_PREFIX[] = "GET /generate/";
const size_t REQUEST_PREFIX_LENGTH = sizeof(REQUEST_PREFIX)-1;

void safe_write(int fd, const char *buffer, size_t nbytes) {
    size_t bytes_remaining = nbytes;
    size_t offset = 0;
    while (bytes_remaining) {
        ssize_t written = write(fd, buffer + offset, bytes_remaining);
        if (written == -1) {
            //Do we need error handling? Don't think so!
            printf("Write failed: %i\n", errno);
            return;
        } 
        bytes_remaining -= written;
        offset += written;
    }
}

void *respond_400(int client_fd) {
    safe_write(client_fd, RESPONSE_400, RESPONSE_400_LENGTH);
    close(client_fd);
    return 0;
}

void *respond_200(int client_fd, size_t body_size, const char *body) {
    char contentSizeBuffer[21]; //Enough for responses up a few kb in size
    safe_write(client_fd, RESPONSE_200_PRELUDE, RESPONSE_200_PRELUDE_LENGTH);
    
    int contentSizeSize = snprintf(contentSizeBuffer, sizeof(contentSizeBuffer), "Content-Size: %zu\n\n", body_size);
    safe_write(client_fd, contentSizeBuffer, contentSizeSize);

    safe_write(client_fd, body, body_size);

    close(client_fd);
    return 0;
}

void *handle_request(void *client_fd_as_voidp) {
    int client_fd = (int)client_fd_as_voidp;

    //long enough for 'GET /generate/18446744073709551616 HTTP/1.1\n\n\0'
    // (i.e. we could handle inputs up to 64 bit)
    const int max_line_length = 45; 
    char req_buffer[max_line_length+1];

    int nread = 0;
    int newline = 0;

    while (nread < max_line_length && !newline) {
        int more_bytes = read(client_fd, req_buffer + nread, max_line_length - nread);
        if (more_bytes == -1 && errno == ECONNRESET) {
            //Connection reset by peer. nobody cares about our response.
            return 0;
        }

        int end = nread+more_bytes;
        for (int i=nread; i<end; i++) {
            char c = req_buffer[i+nread];
            if (c == '\n' || c == '\r') {
                newline = 1;
                nread = i;
                req_buffer[i] = 0;
                break;
            }
        }
        if (!newline) {
            nread += more_bytes;
        }
    }    

    req_buffer[max_line_length] = 0;
    if (!newline 
        || nread < REQUEST_PREFIX_LENGTH
        || memcmp(req_buffer, REQUEST_PREFIX, REQUEST_PREFIX_LENGTH) != 0) {
        //Error case. Client is allowed to be unhappy, just dump a 400 response and hope for the best.
        return respond_400(client_fd);
    }

    factor_int_t req_n = (factor_int_t)strtoimax(req_buffer + REQUEST_PREFIX_LENGTH, NULL, 10);
    if (req_n == 0) {
        //Still an error.
        return respond_400(client_fd);
    }

    //This is going to be a happy response. 
    //Need to read full request, otherwise clients report errors :(
    int last_read_was_nl = req_buffer[nread-1] == '\n';
    char nl_find_buffer = 0;
    //This can be done faster! (avoid unnecessary syscalls, use maximally large read()s.)
    while (1) {
        int more_bytes = read(client_fd, &nl_find_buffer, 1);
        if (more_bytes == -1 && errno == ECONNRESET) {
            //Client is not listening any more.
            return 0;
        }
        if (more_bytes <= 0) {
            break;
        }
        if(nl_find_buffer == '\n' && last_read_was_nl) {
            break;
        }
        if (nl_find_buffer != '\r') {
            last_read_was_nl = nl_find_buffer == '\n';
        }
    }

    sized_factors_t factors;
    int n_factors = factor(req_n, factors);
    char output_buffer[8192]; //8kb should be enough for everyone :)
    output_buffer[0] = '[';
    char *buffer_cur = output_buffer + 1;
    for (int i=0; i<n_factors; i++) {
        if (i != 0) {
            *(buffer_cur++) = ',';
        }
        int factor_str_length = sprintf(buffer_cur, "%"PRIu64, (uint64_t)factors[i]);
        buffer_cur += factor_str_length;
    }
    *(buffer_cur++) = ']';

    return respond_200(client_fd, (buffer_cur-output_buffer), output_buffer);
}

static int socket_fd;
void cleanup() {
    if(socket_fd > -1) {
        close(socket_fd);
    }
}

int main(int argc, char *argv[]) {
    socket_fd = socket(AF_INET, SOCK_STREAM, 0);
    if (socket_fd == -1) {
        printf("socket creation failed.");
        return 1;
    }
    atexit(cleanup);

    struct sockaddr_in serv_addr, client;
    bzero(&serv_addr, sizeof(serv_addr));
    serv_addr.sin_family = AF_INET;
    serv_addr.sin_addr.s_addr = htonl(INADDR_ANY);
    serv_addr.sin_port = htons(PORT);

    if ((bind(socket_fd, (struct sockaddr*)&serv_addr, sizeof(serv_addr))) != 0) { 
        printf("socket bind on 0.0.0.0:%i failed...\n", PORT); 
        return 1;
    } 

    if ((listen(socket_fd, 5)) != 0) { 
        printf("Listen failed...\n"); 
        return 1;
    } 
    printf("Listening on 0.0.0.0:%i\n", PORT);

    unsigned len = sizeof(client);
    while (1) {
        int client_fd = accept(socket_fd, (struct sockaddr*)&client, &len);
        if (client_fd < 0) {
            printf("accept() failed.\n");
            return 1;
        }
        pthread_t thread;
        pthread_create(&thread, NULL, handle_request, (void*)(int64_t)client_fd);
        pthread_detach(thread);
    }
    
}