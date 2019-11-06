# Lasttests

Dieser Ordner enthält das Locust- und Dockerfile um Metriken der verschiedenen Abgaben zu sammeln. 

### Setup

```bash
docker build . -t locust-coding-challenge
docker run -p 8089:8089 locust-coding-challengee
```

[Dokumentation](https://docs.locust.io/en/stable/index.html)
