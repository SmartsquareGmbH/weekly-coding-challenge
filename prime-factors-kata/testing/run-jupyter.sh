#!/usr/bin/env bash
set -e

docker run --rm \
    -eJUPYTER_ENABLE_LAB=yes \
    -v "$PWD":/home/jovyan/work \
    -p8888:8888 \
    jupyter/scipy-notebook