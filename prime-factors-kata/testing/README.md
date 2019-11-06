# Lasttests

Dieser Ordner enhält das Locustfile und das dazugehörige Dockerfile um die Abgaben zu testen zu die Metriken zu vergleichen.

### Ausführung
```bash
docker build . -t locust-coding-challenge
docker run --netword host locust-coding-challenge
```
