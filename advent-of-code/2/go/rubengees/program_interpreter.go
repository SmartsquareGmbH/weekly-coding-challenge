package main

import (
	"errors"
	"fmt"
)

// Runs the given program and returns the resulting program memory.
// What the program does is defined by the opcode. The first opcode is at position 0 and the arguments for the command
// defined by that opcode are behind the opcode. The amount of arguments is defined by the opcode.
// Once the command is run, the instruction pointer moves behind the instruction and checks for the next opcode.
// Once the opcode 99 is detected, the result is returned. If no such opcode is found, an error is returned.
//
// Possible opcodes:
// 1 - Adds the values at the position of the first and second arguments and saves the result in the third argument.
// 2 - Multiplies the values at the position of the first and second arguments and saves the result in the third argument.
// 99 - Terminates the program.
func runProgram(program []int) (result []int, err error) {
	if len(program) <= 0 {
		return nil, errors.New("program is empty")
	}

	result = program
	instructionPointer := 0

	for instructionPointer < len(result) {
		opcode := result[instructionPointer]

		if opcode == 99 {
			return result, nil
		} else {
			// We have read the opcode. Advance the instruction pointer by one.
			instructionPointer += 1

			if opcode == 1 {
				// Adds the values at the positions given by the first and second arg
				//and writes the result to the position given by the third arg.
				arguments, err := readArguments(program, instructionPointer, 3)
				if err != nil {
					return nil, err
				}

				result[arguments[2]] = result[arguments[0]] + result[arguments[1]]
				instructionPointer += 3
			} else if opcode == 2 {
				// Multiplies the values at the positions given by the first and second arg
				//and writes the result to the position given by the third arg.
				arguments, err := readArguments(program, instructionPointer, 3)
				if err != nil {
					return nil, err
				}

				result[arguments[2]] = result[arguments[0]] * result[arguments[1]]
				instructionPointer += 3
			} else {
				return nil, errors.New(fmt.Sprintf("unknown opcode detected: %d", opcode))
			}
		}
	}

	return nil, errors.New("no terminating opcode (99) found in program")
}

func readArguments(program []int, instructionPointer int, count int) (args []int, err error) {
	if instructionPointer+count >= len(program) {
		return nil, errors.New("program ends before %d arguments could be read")
	}

	var result []int

	for i := 0; i < count; i++ {
		result = append(result, program[instructionPointer+i])
	}

	return result, nil
}
