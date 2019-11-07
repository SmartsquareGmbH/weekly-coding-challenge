# Lasttests

Dieser Ordner enhält das Locustfile und das dazugehörige Dockerfile um die Abgaben zu testen und die Metriken zu vergleichen.

### Ausführung
```bash
docker build . -t locust-coding-challenge
docker run --network host locust-coding-challenge
```
