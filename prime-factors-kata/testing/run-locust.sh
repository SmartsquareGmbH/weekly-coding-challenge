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
docker run ${DOCKER_ARGS[@]} -v "$(pwd)/csv:/csv" --rm --name locust-coding-challenge locust-coding-challenge "--host=$TARGET_HOST" --csv=/csv/$NAME
