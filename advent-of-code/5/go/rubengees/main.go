package main

import (
	"io/ioutil"
	"log"
	"strconv"
	"strings"
)

func main() {
	file := readFile("input.txt")
	program := parse(file)

	_, err := runProgram(program)
	if err != nil {
		log.Fatal(err)
	}
}

func readFile(filename string) string {
	byteArray, err := ioutil.ReadFile(filename)
	if err != nil {
		log.Fatal(err)
	}

	return string(byteArray)
}

func parse(input string) []int {
	split := strings.Split(strings.Trim(input, "\n"), ",")
	result := make([]int, 0, len(split))

	for _, part := range split {
		intPart, err := strconv.Atoi(part)
		if err != nil {
			log.Fatal(err)
		}

		result = append(result, intPart)
	}

	return result
}
