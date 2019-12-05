package main

import (
	"errors"
	"fmt"
	"log"
	"math"
)

type Opcode struct {
	value         int
	argumentMode1 int
	argumentMode2 int
	argumentMode3 int
}

type Arguments struct {
	argument1 int
	argument2 int
	argument3 int
}

// I can't think of a worse way to write this. Thanks go for not having contains on slices.
var validOpcodes = map[int]struct{}{1: {}, 2: {}, 3: {}, 4: {}, 5: {}, 6: {}, 7: {}, 8: {}, 99: {}}

// This is really bad, but I had no more time and/or motivation.
// Go's support for nothing out of the box is not helping.
func runProgram(program []int) (result []int, err error) {
	if len(program) <= 0 {
		return nil, errors.New("program is empty")
	}

	result = program
	instructionPointer := 0

	for instructionPointer < len(result) {
		opcode, err := parseOpcode(result[instructionPointer])
		if err != nil {
			return result, err
		}

		if opcode.value == 99 {
			return result, nil
		} else {
			// We have read the opcode. Advance the instruction pointer by one.
			instructionPointer += 1

			if opcode.value == 1 {
				args, err := readArguments(program, instructionPointer, opcode)
				if err != nil {
					return nil, err
				}

				result[args.argument3] = args.argument1 + args.argument2
				instructionPointer += 3
			} else if opcode.value == 2 {
				args, err := readArguments(program, instructionPointer, opcode)
				if err != nil {
					return nil, err
				}

				result[args.argument3] = args.argument1 * args.argument2
				instructionPointer += 3
			} else if opcode.value == 3 {
				args, err := readArguments(program, instructionPointer, opcode)
				if err != nil {
					return nil, err
				}

				var input int
				print("ID? ")
				if _, err := fmt.Scan(&input); err != nil {
					log.Fatal("Please input a valid integer ", err)
				}

				program[args.argument1] = input
				instructionPointer += 1
			} else if opcode.value == 4 {
				args, err := readArguments(program, instructionPointer, opcode)
				if err != nil {
					return nil, err
				}

				println(args.argument1)
				instructionPointer += 1
			} else if opcode.value == 5 {
				args, err := readArguments(program, instructionPointer, opcode)
				if err != nil {
					return nil, err
				}

				if args.argument1 != 0 {
					instructionPointer = args.argument2
				} else {
					instructionPointer += 2
				}
			} else if opcode.value == 6 {
				args, err := readArguments(program, instructionPointer, opcode)
				if err != nil {
					return nil, err
				}

				if args.argument1 == 0 {
					instructionPointer = args.argument2
				} else {
					instructionPointer += 2
				}
			} else if opcode.value == 7 {
				args, err := readArguments(program, instructionPointer, opcode)
				if err != nil {
					return nil, err
				}

				if args.argument1 < args.argument2 {
					program[args.argument3] = 1
				} else {
					program[args.argument3] = 0
				}

				instructionPointer += 3
			} else if opcode.value == 8 {
				args, err := readArguments(program, instructionPointer, opcode)
				if err != nil {
					return nil, err
				}

				if args.argument1 == args.argument2 {
					program[args.argument3] = 1
				} else {
					program[args.argument3] = 0
				}

				instructionPointer += 3
			} else {
				return nil, errors.New(fmt.Sprintf("unknown opcode detected: %d", opcode))
			}
		}
	}

	return nil, errors.New("no terminating opcode (99) found in program")
}

func parseOpcode(number int) (opcode Opcode, err error) {
	digitAmount := countDigits(number)
	value := digit(number, 1)
	argumentMode1 := 0
	argumentMode2 := 0
	argumentMode3 := 0

	if digitAmount >= 2 {
		value += digit(number, 2) * 10
	}

	if digitAmount >= 3 {
		argumentMode1 = digit(number, 3)
	}

	if digitAmount >= 4 {
		argumentMode2 = digit(number, 4)
	}

	if digitAmount >= 5 {
		argumentMode3 = digit(number, 5)
	}

	if _, ok := validOpcodes[value]; !ok {
		return opcode, errors.New(fmt.Sprintf("unknown opcode: %d", value))
	}

	opcode = Opcode{
		value:         value,
		argumentMode1: argumentMode1,
		argumentMode2: argumentMode2,
		argumentMode3: argumentMode3,
	}

	return opcode, nil
}

func readArguments(program []int, instructionPointer int, opcode Opcode) (args Arguments, err error) {
	switch opcode.value {
	case 3:
		if instructionPointer >= len(program) {
			return args, errors.New("program ends before 1 argument could be read")
		}

		args.argument1 = resolveArgument(program, instructionPointer, 1)
	case 4:
		if instructionPointer >= len(program) {
			return args, errors.New("program ends before 1 argument could be read")
		}

		args.argument1 = resolveArgument(program, instructionPointer, opcode.argumentMode1)
	case 5, 6:
		if instructionPointer+2 >= len(program) {
			return args, errors.New("program ends before 2 arguments could be read")
		}

		args.argument1 = resolveArgument(program, instructionPointer, opcode.argumentMode1)
		args.argument2 = resolveArgument(program, instructionPointer+1, opcode.argumentMode2)
	case 1, 2, 7, 8:
		if instructionPointer+2 >= len(program) {
			return args, errors.New("program ends before 3 arguments could be read")
		}

		args.argument1 = resolveArgument(program, instructionPointer, opcode.argumentMode1)
		args.argument2 = resolveArgument(program, instructionPointer+1, opcode.argumentMode2)
		args.argument3 = resolveArgument(program, instructionPointer+2, 1)
	}

	return args, nil
}

func resolveArgument(program []int, position int, argumentMode int) int {
	if argumentMode == 1 {
		return program[position]
	} else if argumentMode == 0 {
		return program[program[position]]
	}

	panic(fmt.Sprintf("unknown argumentMode: %d", argumentMode))
}

func countDigits(number int) (count int) {
	for number != 0 {
		number /= 10
		count++
	}

	return count
}

func digit(num, place int) int {
	r := num % int(math.Pow(10, float64(place)))
	return r / int(math.Pow(10, float64(place-1)))
}
