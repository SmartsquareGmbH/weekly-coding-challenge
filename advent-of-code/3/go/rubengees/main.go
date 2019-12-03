package main

import (
	"bufio"
	"log"
	"os"
	"strconv"
	"strings"
)

func main() {
	rawPaths := readFile("input.txt")
	paths := make([]Path, 0)

	for _, rawPath := range rawPaths {
		paths = append(paths, parse(rawPath))
	}

	if len(paths) != 2 {
		log.Fatalf("invalid amount of paths: %d", len(paths))
	}

	closestIntersection, err := findClosestIntersection(paths[0], paths[1])

	if err != nil {
		log.Fatal(err)
	} else {
		println(closestIntersection.manhattanDistance)
	}

	shortestIntersection, err := findShortestIntersection(paths[0], paths[1])

	if err != nil {
		log.Fatal(err)
	} else {
		println(shortestIntersection.steps)
	}
}

func readFile(filename string) []string {
	file, err := os.Open(filename)
	if err != nil {
		log.Fatal(err)
	}

	var lines []string
	scanner := bufio.NewScanner(file)

	for scanner.Scan() {
		lines = append(lines, scanner.Text())
	}

	return lines
}

// Parses the given input into a path.
func parse(input string) Path {
	split := strings.Split(input, ",")
	var result Path

	for _, part := range split {
		if len(part) <= 1 {
			log.Fatalf("invalid input: %s", part)
		}

		var direction Direction
		switch part[0:1] {
		case "U":
			direction = UP
		case "R":
			direction = RIGHT
		case "D":
			direction = DOWN
		case "L":
			direction = LEFT
		default:
			log.Fatalf("unknown direction in input: %s", part)
		}

		amount, err := strconv.Atoi(part[1:])
		if err != nil {
			log.Fatalf("encountered non number part in input: %s", part)
		}

		pathPart := PathPart{
			direction: direction,
			amount:    amount,
		}

		result = append(result, pathPart)
	}

	return result
}
