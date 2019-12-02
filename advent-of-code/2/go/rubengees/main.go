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

	// Additional adjustments as in the task.
	program[1] = 12
	program[2] = 2

	result, err := runProgram(program)
	if err != nil {
		log.Fatal(err)
	}

	fmt.Printf("%d\n", result[0])
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

func runProgram(program []int) (result []int, err error) {
	result = program

	for i := 0; i < len(result); i += 4 {
		opcode := result[i]

		if opcode == 99 {
			return result, nil
		} else {
			inputPosition1 := result[i+1]
			inputPosition2 := result[i+2]
			outputPosition := result[i+3]

			if opcode == 1 {
				result[outputPosition] = result[inputPosition1] + result[inputPosition2]
			} else if opcode == 2 {
				result[outputPosition] = result[inputPosition1] * result[inputPosition2]
			} else {
				return nil, errors.New(fmt.Sprintf("Unknown opcode detected: %d", opcode))
			}
		}
	}

	return nil, errors.New("no terminating opcode (99) found in program")
}
