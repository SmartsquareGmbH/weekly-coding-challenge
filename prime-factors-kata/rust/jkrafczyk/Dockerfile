FROM rust:1.38-alpine

WORKDIR /usr/src/factoring

COPY . .

RUN cargo install --path .

EXPOSE 8080

CMD ["factoring"]