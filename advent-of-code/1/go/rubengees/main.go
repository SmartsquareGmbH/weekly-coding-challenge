package main

import (
	"bufio"
	"fmt"
	"log"
	"os"
	"strconv"
)

func main() {
	numbers := readNumbers("input.txt")

	part1(numbers)
	part2(numbers)
}

// Read all numbers from the given filePath and returns them as an array of float64s.
func readNumbers(filePath string) []float64 {
	var result []float64

	file, err := os.Open(filePath)

	if err != nil {
		log.Fatal(err)
	}

	scanner := bufio.NewScanner(file)

	for scanner.Scan() {
		line := scanner.Text()
		lineNumber, err := strconv.ParseFloat(line, 64)
		if err != nil {
			log.Fatal(err)
		}

		result = append(result, lineNumber)
	}

	if err := scanner.Err(); err != nil {
		log.Fatal(err)
	}

	if err := file.Close(); err != nil {
		log.Fatal(err)
	}

	return result
}

func part1(numbers []float64) {
	calculatedSum := 0

	for _, number := range numbers {
		calculatedSum += calculate(number)
	}

	fmt.Printf("%d\n", calculatedSum)
}

func part2(numbers []float64) {
	calculatedSum := 0

	for _, number := range numbers {
		calculatedSum += calculateRecursive(number)
	}

	fmt.Printf("%d\n", calculatedSum)
}

// Calculate the fuel needed by the given module and returns the amount as an int.
func calculate(module float64) int {
	divisionResult := module / 3.0
	rounded := int(divisionResult)

	return rounded - 2
}

// Calculate the fuel needed by the given module recursively and returns the amount as an int.
// Recursively means, that for each fuel, the fuel is taken as the next module and all results are summed.
func calculateRecursive(module float64) int {
	result := calculate(module)

	if result >= 1 {
		return result + calculateRecursive(float64(result))
	} else {
		return 0
	}
}
