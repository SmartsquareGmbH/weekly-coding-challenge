FROM alpine:latest AS builder

RUN apk add clang make musl-dev binutils gcc

WORKDIR /app
COPY . .

RUN make
RUN ["./factor_test"]



FROM alpine:latest
COPY --from=builder /app/factor /app/factor
EXPOSE 8080
ENTRYPOINT [ "/app/factor" ]