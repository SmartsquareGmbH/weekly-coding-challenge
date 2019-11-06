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

const int PORT = 8080;
const int NUM_THREADS = 150;
const int ACCEPT_QUEUE_LENGTH = (NUM_THREADS+5);
static int socket_fd;


#ifdef DEBUG
#define DLOG(f_, ...) printf((f_), __VA_ARGS__)
const char RESPONSE_400[] = "HTTP/1.1 400 Bad Request\nContent-Type: application/json\nConnection: close\nContent-Length: 2\n\n[]";
const size_t RESPONSE_400_LENGTH = sizeof(RESPONSE_400)-1; //Skip trailing 0 byte!
#else
#define DLOG(f, ...) 
#endif

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

int respond_400(int client_fd) {
    #ifdef DEBUG
    safe_write(client_fd, RESPONSE_400, RESPONSE_400_LENGTH);
    #endif
    close(client_fd);
    return 0;
}

int respond_200(int client_fd, size_t body_size, const char *body) {
    char contentSizeBuffer[21]; //Enough for responses up a few kb in size
    safe_write(client_fd, RESPONSE_200_PRELUDE, RESPONSE_200_PRELUDE_LENGTH);
    
    int contentSizeSize = snprintf(contentSizeBuffer, sizeof(contentSizeBuffer), "Content-Size: %zu\n\n", body_size);
    safe_write(client_fd, contentSizeBuffer, contentSizeSize);

    safe_write(client_fd, body, body_size);

    close(client_fd);
    return 0;
}

int handle_request(int client_fd) {
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
    char nl_find_buffer[1024];
    int done = 0;

    while (!done) {
        int more_bytes = read(client_fd, nl_find_buffer, sizeof(nl_find_buffer));
        if (more_bytes == -1 && errno == ECONNRESET) {
            //Client is not listening any more.
            return 0;
        }
        if (more_bytes <= 0) {
            break;
        }
        for (int i=0; i<more_bytes; i++) {
            char c = nl_find_buffer[i];
            if(c == '\n' && last_read_was_nl) {
                done = 1;
                break;
            }
            if (c != '\r') {
                last_read_was_nl = c == '\n';
            }
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


int accept_and_handle_request(int thread_index, int fd) {
    struct sockaddr_in client;
    socklen_t len = sizeof(client);
    int client_fd = accept(fd, (struct sockaddr*)&client, &len);
    if (client_fd < 0) {
        printf("accept() failed for thread %i / fd %i: %i.\n", thread_index, fd, errno);
        return 1;
    }
    DLOG("Handling connection in thread %i\n", thread_index);
    handle_request(client_fd);
    return 0;
}

int setup_socket(int thread_index) {

    int my_listen_fd = socket(AF_INET, SOCK_STREAM, 0);
    if (my_listen_fd == -1) {
        printf("socket creation failed for thread %i: %i", thread_index, errno);
        return 0;
    }

    int optval = 1;
    if (setsockopt(my_listen_fd, SOL_SOCKET, SO_REUSEPORT, &optval, sizeof(optval))) {
        printf("setsockopt(SO_REUSEPORT) failed for thread %i: %i\n", thread_index, errno);
        return 0;
    }

    struct sockaddr_in serv_addr;
    bzero(&serv_addr, sizeof(serv_addr));
    serv_addr.sin_family = AF_INET;
    serv_addr.sin_addr.s_addr = htonl(INADDR_ANY);
    serv_addr.sin_port = htons(PORT);

    if ((bind(my_listen_fd, (struct sockaddr*)&serv_addr, sizeof(serv_addr))) != 0) { 
        printf("socket bind on 0.0.0.0:%i failed for thread %i: %i...\n", PORT, thread_index, errno); 
        return 0;
    } 

    if ((listen(my_listen_fd, ACCEPT_QUEUE_LENGTH)) != 0) { 
        printf("Listen failed for thread %i: %i.\n", thread_index, errno); 
        return 0;
    } 

    printf("Thread %i listening on 0.0.0.0:%i\n", thread_index, PORT);

    return my_listen_fd;
}

void *listen_and_accept_requests(void *thread_index_as_voidp) {
    int thread_index = (int)thread_index_as_voidp;
    //int my_listen_fd = setup_socket(thread_index);
    int my_listen_fd = socket_fd;

    DLOG("Thread %i starting to handle requests.\n", thread_index);
    while (!accept_and_handle_request(thread_index, my_listen_fd)) {
    }

    printf("Thread %i terminating.\n", thread_index);
    return 0;
}



int main(int argc, char *argv[]) {
    socket_fd = setup_socket(0);

    for (int i=1; i<=NUM_THREADS; i++) {
        pthread_t thread;
        pthread_create(&thread, NULL, listen_and_accept_requests, (void*)(int64_t)i);
        pthread_detach(thread);
    }

    printf("Ready.\n");
    while(1);
    while (!accept_and_handle_request(0, socket_fd)) {
    }
}