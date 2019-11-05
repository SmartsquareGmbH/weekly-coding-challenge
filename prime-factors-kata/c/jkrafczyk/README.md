# Buildprozess

## Nativ

Anforderungen: Installiertes clang, libc, pthreads und gnu make.

Während der Entwicklung können mit `make test` die Unit-Tests aus factor_test.c oder mit `make run` der Server ausgeführt werden.  
Zum erstellen des ausführbaren Artefakts `make all` verwenden.

## Docker

`docker build . -t prime-factors-c-jkrafczyk && docker run --rm -p8080:8080 --name prime-factors-c-jkrafczyk prime-factors-c-jkrafczyk`