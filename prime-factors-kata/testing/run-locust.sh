#!/usr/bin/env bash

if [[ -n "$1" ]]
then
    NAME="$1"
fi

if [[ -z "$NAME" ]]
then
    NAME="$(date +%Y%m%d%H%M%S)"
fi

if [[ -z "$TARGET_HOST" ]]
then
    if [[ "$(uname)" = "Darwin" ]]
    then
        TARGET_HOST="http://host.docker.internal:8080"
    else
        TARGET_HOST="http://localhost:8080"
    fi  
fi

if [[ "$(uname)" = "Darwin" ]]
then
    DOCKER_ARGS=( )
else
    DOCKER_ARGS=( --network host )
fi

docker build . -t locust-coding-challenge
run_locust() {
    local run=$1
    shift
    mkdir -p csv/$NAME/$run logs/$NAME
    docker run ${DOCKER_ARGS[@]} \
        -v "$(pwd)/csv:/csv" \
        --rm \
        locust-coding-challenge \
        "--host=$TARGET_HOST" \
        --csv=/csv/$NAME/$run/locust \
        $@ 2>&1 | tee logs/$NAME/$run.log
}

run_locust 0050 -c=50  -r=5  -t=45s
run_locust 0100 -c=100 -r=10 -t=45s
run_locust 0200 -c=200 -r=20 -t=45s
run_locust 0300 -c=300 -r=30 -t=45s
run_locust 0500 -c=500 -r=50 -t=45s
run_locust 0750 -c=750 -r=75 -t=45s
run_locust 1000 -c=1000 -r=50 -t=45s