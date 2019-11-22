#!/usr/bin/env bash
#This is rouhgly based on Json Wilsons Java solution.
set -e
SQ=$(printf "\x27")

LINES=(
    '#!/usr/bin/env bash'
    'set -e'
    'SQ=$(printf "\x27")'
    ''
    'LINES=('
    ')'
    ''
    'for i in $(seq 0 4); do'
    '    echo "${LINES[$i]}"'
    'done'
    'for i in $(seq 0 $[${#LINES[@]}-1]); do'
    '    echo "    $SQ${LINES[$i]}$SQ"'
    'done'
    'for i in $(seq 5 $[${#LINES[@]}-1]); do'
    '    echo "${LINES[$i]}"'
    'done'
)

for i in $(seq 0 4); do
    echo "${LINES[$i]}"
done
for i in $(seq 0 $[${#LINES[@]}-1]); do
    echo "    $SQ${LINES[$i]}$SQ"
done
for i in $(seq 5 $[${#LINES[@]}-1]); do
    echo "${LINES[$i]}"
done
