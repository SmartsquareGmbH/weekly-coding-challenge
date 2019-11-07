#!/usr/bin/env bash

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

docker run \
    --rm \
    -ti \
    ${DOCKER_ARGS[@]} \
    -e "JAVA_OPTS=-Dgatling.users=50 -Dgatling.userRampUp=30 -Dgatling.duration=60 -Dgatling.target=${TARGET_HOST}" \
    -v "$(pwd)/gatling:/opt/gatling/primekata" \
    -v "$(pwd)/gatling-results:/opt/gatling/results" \
    denvazh/gatling \
    -sf "/opt/gatling/primekata" \
    -rsf "/opt/gatling/primekata" \
    -rd "Load Test"  
    