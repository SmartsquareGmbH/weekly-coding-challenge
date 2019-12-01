package main

import (
	"fmt"
	"testing"
)

var data = []struct {
	in  float64
	out int
}{
	{12.0, 2},
	{14.0, 2},
	{1969.0, 654},
	{100756.0, 33583},
}

func TestCalculate(t *testing.T) {
	for _, row := range data {
		t.Run(fmt.Sprintf("%g", row.in), func(t *testing.T) {
			result := calculate(row.in)

			if result != row.out {
				t.Errorf("Expected %d, but got %d", row.out, result)
			}
		})
	}
}

var dataRecursive = []struct {
	in  float64
	out int
}{
	{14.0, 2},
	{1969.0, 966},
	{100756.0, 50346},
}

func TestCalculateRecursive(t *testing.T) {
	for _, row := range dataRecursive {
		t.Run(fmt.Sprintf("%g", row.in), func(t *testing.T) {
			result := calculateRecursive(row.in)

			if result != row.out {
				t.Errorf("Expected %d, but got %d", row.out, result)
			}
		})
	}
}
