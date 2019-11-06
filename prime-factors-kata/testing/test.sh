#!/usr/bin/env bash
set -e

if [[ -z "$URL" ]]
then
    URL="http://localhost:8080/generate/"
fi

failures=0

if ! which jq >& /dev/null
then
    echo "jq needs to be in path to execute tests."
    exit 1
fi

factor() {
    #convert multi-line output to single line.
    echo $(curl -s "$URL$1" | jq -r ".[]" )
}

test_fail() {
    failures=$[ $failures + 1 ]
    local name="$1"
    shift
    printf "[\033[31;1m✕\033[0m] \033[37;1m$name\033[1m - \033[0m$*\033[0m\n"
}

test_ok() {
    local name="$1"
    printf "[\033[32;1m✓\033[0m] \033[37;1m$name\033[1m\n"
}

# This only asserts that the product of all returned factors produces the correct result.
assert_result_correct() {
    local n="$1"
    local actual="$(factor $n)"
    local test_name="$n"

    local prod=1
    local f
    for f in $actual
    do
        prod=$[$prod * $f]
    done
    if [[ "$prod" -ne "$n" ]]
    then
        test_fail "$test_name" "Factored $n to '$actual', which produces '$prod' when multiplied."
        return
    fi

    test_ok "$test_name" 
}

# This asserts that exactly the specified factors, in the specified order are returned.
assert_factors() {
    local n="$1"
    shift
    local expected="$*"
    local actual="$(factor $n)"
    local test_name="$n: $expected"
    
    local prod=1
    local f
    for f in $actual
    do
        prod=$[$prod * $f]
    done
    if [[ "$prod" -ne "$n" ]]
    then
        test_fail "$test_name" "Factored $n to '$actual', which produces '$prod' when multiplied."
        return
    fi

    if [[ "$expected" != "$actual" ]]
    then
        test_fail "$test_name" "Expected factors of $n to be '$expected', but got '$actual'."
        return
    fi


    test_ok "$test_name" 
}

#Check correct factoring with a hand full of carefully crafted examples
assert_factors 1 
assert_factors 2 2
assert_factors 3 3 
assert_factors 4 2 2
assert_factors 5 5
assert_factors 6 2 3
assert_factors 9 3 3
assert_factors 10 2 5
assert_factors 256 2 2 2 2 2 2 2 2
assert_factors 257 257
assert_factors 74859 3 24953
assert_factors $[65536*65536-1] 3 5 17 257 65537

if [[ "$failures" -eq "0" ]]
then
    #If that worked out, do additional tests on random 32-bit integers
    for i in {0..25}
    do
        assert_result_correct $[ $RANDOM % 4294967294 + 1]
    done
fi

if [[ "$failures" -ne "0" ]]
then
    echo "There were test failures."
    exit 1
fi
