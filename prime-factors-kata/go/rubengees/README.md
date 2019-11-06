# Setup

### Nativ

```bash
go mod download
go run main.go
```

### Docker

```bash
docker run --network host $(docker build -q .)
```