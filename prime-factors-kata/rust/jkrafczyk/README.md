# Buildprozess

## Nativ

Anforderungen: Installiertes aktuelles rust mit cargo.  
Während der Entwicklung mit `cargo build` kompilieren und die Anwendung mit `target/debug/factoring` starten.
Optimierte release-version für das Lasttestszenario mit `cargo build --release` kompilieren und mit `target/release/factoring` starten.

## Docker

`docker build . -t prime-factors-rust-jkrafczyk && docker run --rm -p8080:8080 --name prime-factors-rust-jkrafczyk prime-factors-rust-jkrafczyk`