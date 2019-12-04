package main

import (
	"math"
)

func main() {
	counterPart1 := 0
	for i := 134564; i <= 585159; i++ {
		if onlyIncreases(i) && hasPair(i) {
			counterPart1++
		}
	}

	println(counterPart1)

	counterPart2 := 0
	for i := 134564; i <= 585159; i++ {
		if onlyIncreases(i) && hasPairWithoutGroup(i) {
			counterPart2++
		}
	}

	println(counterPart2)
}

func onlyIncreases(number int) bool {
	for i := 6; i > 1; i-- {
		if digit(number, i) > digit(number, i-1) {
			return false
		}
	}

	return true
}

func hasPair(number int) bool {
	for i := 6; i > 1; i-- {
		if digit(number, i) == digit(number, i-1) {
			return true
		}
	}

	return false
}

func hasPairWithoutGroup(number int) bool {
	for i := 6; i > 1; i-- {
		if digit(number, i) == digit(number, i-1) {
			// Is the previous digit and the digit after not the same ?
			if (i <= 2 || digit(number, i-2) != digit(number, i)) && (i >= 6 || digit(number, i+1) != digit(number, i)) {
				return true
			}
		}
	}

	return false
}

func digit(num, place int) int {
	r := num % int(math.Pow(10, float64(place)))
	return r / int(math.Pow(10, float64(place-1)))
}
