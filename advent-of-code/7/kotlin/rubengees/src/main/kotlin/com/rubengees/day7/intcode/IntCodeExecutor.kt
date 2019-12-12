package com.rubengees.day7.intcode

import com.rubengees.day7.intcode.Opcode.OpcodeResult.HaltProgram
import com.rubengees.day7.intcode.Opcode.OpcodeResult.InstructionPointerModification
import com.rubengees.day7.intcode.Opcode.OpcodeResult.Output
import com.rubengees.day7.intcode.Opcode.OpcodeResult.ProgramModification

object IntCodeExecutor {

    fun run(input: Program, inputInstructionPointer: Int = 0, pauseOnOutput: Boolean = false): ProgramExecutionResult {
        var outputs = emptyList<Int>()
        var instructionPointer = inputInstructionPointer
        var program = input

        while (instructionPointer < program.length) {
            val opcode = Opcode.parse(program[instructionPointer])
            val args = Argument.parse(program, opcode.argumentCount, instructionPointer)

            when (val result = opcode.execute(program, args)) {
                is ProgramModification -> {
                    program = result.newProgram
                    instructionPointer += opcode.argumentCount + 1
                }
                is InstructionPointerModification -> {
                    instructionPointer = result.newInstructionPointer
                }
                is Output -> {
                    outputs = outputs + result.value
                    instructionPointer += opcode.argumentCount + 1

                    if (pauseOnOutput) {
                        return ProgramExecutionResult(program, instructionPointer, outputs)
                    }
                }
                is HaltProgram -> {
                    return ProgramExecutionResult(program, instructionPointer + 1, outputs, true)
                }
                else -> {
                    instructionPointer += opcode.argumentCount + 1
                }
            }
        }

        error("Program exited without HALT opcode.")
    }

    data class ProgramExecutionResult(
        val program: Program,
        val instructionPointer: Int = 0,
        val outputs: List<Int> = emptyList(),
        val isHalted: Boolean = false
    )
}
