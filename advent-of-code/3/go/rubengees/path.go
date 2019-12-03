package main

import (
	"errors"
	"fmt"
)

type Direction int

// This seems to be the way to do enums in go.
const (
	UP Direction = iota
	RIGHT
	DOWN
	LEFT
)

type Path []PathPart

type PathPart struct {
	direction Direction
	amount    int
}

type Intersection struct {
	x                 int
	y                 int
	manhattanDistance int
}

// Looks for intersections in the given paths and returns the closest to the central point.
func findClosestIntersection(path1 Path, path2 Path) (result Intersection, err error) {
	intersections := findIntersections(path1, path2)

	var closestIntersection Intersection
	for _, intersection := range intersections {
		if closestIntersection.manhattanDistance == 0 || intersection.manhattanDistance < closestIntersection.manhattanDistance {
			closestIntersection = intersection
		}

		println(
			fmt.Sprintf(
				"found intersection at x: %d, y: %d with manhattan distance %d",
				intersection.x,
				intersection.y,
				intersection.manhattanDistance,
			),
		)
	}

	if closestIntersection.manhattanDistance == 0 {
		return closestIntersection, errors.New("no intersection found for input")
	} else {
		return closestIntersection, nil
	}
}

type Point struct {
	x int
	y int
}

// Looks for intersections in the given paths and returns them as a slice.
func findIntersections(path1 Path, path2 Path) []Intersection {
	path1Points := make(map[Point]bool)
	result := make([]Intersection, 0)

	iteratePoints(path1, func(point Point) {
		path1Points[point] = true
	})

	iteratePoints(path2, func(point Point) {
		if path1Points[point] {
			intersection := Intersection{
				x:                 point.x,
				y:                 point.y,
				manhattanDistance: abs(point.x) + abs(point.y),
			}

			result = append(result, intersection)
		}
	})

	return result
}

// Iterate through all points the given path is taking and calls the callback for each of them.
func iteratePoints(path Path, callback func(point Point)) {
	current := Point{
		x: 0,
		y: 0,
	}
	for _, pathPart := range path {
		switch pathPart.direction {
		case UP:
			for i := 0; i < pathPart.amount; i++ {
				callback(Point{x: current.x, y: current.y + i + 1})
			}
			current.y += pathPart.amount
		case RIGHT:
			for i := 0; i < pathPart.amount; i++ {
				callback(Point{x: current.x + i + 1, y: current.y})
			}
			current.x += pathPart.amount
		case DOWN:
			for i := 0; i < pathPart.amount; i++ {
				callback(Point{x: current.x, y: current.y - i - 1})
			}
			current.y -= pathPart.amount
		case LEFT:
			for i := 0; i < pathPart.amount; i++ {
				callback(Point{x: current.x - i - 1, y: current.y})
			}
			current.x -= pathPart.amount
		}
	}
}

// Why does this language not have abs for ints?
func abs(value int) int {
	if value > 0 {
		return value
	}

	return -value
}
