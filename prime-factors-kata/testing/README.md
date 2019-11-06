# Tests

Dieser Ordner enthält zum einen den Locust Container um Metriken der verschiedenen Abgaben und zum anderen ein Shell Skript um die Funktionalität zu überprüfen.

### Lasttests
```bash
docker build . -t locust-coding-challenge
docker run -p 8089:8089 locust-coding-challenge
```

### Funktionstests
```bash
./test.sh
```

[Dokumentation](https://docs.locust.io/en/stable/index.html)
