package com.rubengees.day9.intcode

import com.rubengees.day9.intcode.Opcode.OpcodeResult.HaltProgram
import com.rubengees.day9.intcode.Opcode.OpcodeResult.InstructionPointerModification
import com.rubengees.day9.intcode.Opcode.OpcodeResult.Output
import com.rubengees.day9.intcode.Opcode.OpcodeResult.ProgramModification

object IntCodeExecutor {

    fun run(input: Program): ProgramExecutionResult {
        var outputs = emptyList<Long>()
        var instructionPointer = 0L
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
                    return ProgramExecutionResult(program, instructionPointer + 1, outputs)
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
        val instructionPointer: Long = 0,
        val outputs: List<Long> = emptyList()
    )
}
