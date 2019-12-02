package main

import (
	"errors"
	"fmt"
	"io/ioutil"
	"log"
	"strconv"
	"strings"
)

func main() {
	fileContent := readFile("input.txt")
	program := parse(fileContent)

	part1Result, err := runProgramWithVerbAndNoun(program, 12, 1)
	if err != nil {
		log.Fatal(err)
	}

	part2Verb, part2Noun, err := findVerbAndNounForResult(program, 19690720)
	if err != nil {
		log.Fatal(err)
	}

	fmt.Println(part1Result)
	fmt.Println(100*part2Verb + part2Noun)
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

func findVerbAndNounForResult(program []int, result int) (verb int, noun int, err error) {
	// The values for verb and noun are defined to be in [0, 100].
	// Iterate over all possibilities and return once found.
	for i := 0; i < 100; i++ {
		for j := 0; j < 100; j++ {
			part2Result, err := runProgramWithVerbAndNoun(program, i, j)
			if err != nil {
				return 0, 0, err
			}

			if part2Result == result {
				return i, j, nil
			}
		}
	}

	return 0, 0, errors.New("no program execution with the result 19690720 found")
}

func runProgramWithVerbAndNoun(program []int, verb int, noun int) (result int, err error) {
	programCopy := append([]int(nil), program...)
	programCopy[1] = verb
	programCopy[2] = noun

	programResult, err := runProgram(programCopy)

	if err != nil {
		return 0, err
	} else {
		return programResult[0], nil
	}
}
