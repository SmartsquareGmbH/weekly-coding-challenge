package com.rubengees.day5.intcode

import com.rubengees.day5.intcode.Opcode.OpcodeResult.HaltProgram
import com.rubengees.day5.intcode.Opcode.OpcodeResult.InstructionPointerModification
import com.rubengees.day5.intcode.Opcode.OpcodeResult.Output
import com.rubengees.day5.intcode.Opcode.OpcodeResult.ProgramModification

object IntCodeExecutor {

    fun run(input: Program): ProgramExecutionResult {
        var outputs = emptyList<Int>()
        var instructionPointer = 0
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
                }
                is HaltProgram -> {
                    return ProgramExecutionResult(program, outputs)
                }
                else -> {
                    instructionPointer += opcode.argumentCount + 1
                }
            }
        }

        error("Program exited without HALT opcode.")
    }

    class ProgramExecutionResult(val program: Program, val outputs: List<Int>)
}
