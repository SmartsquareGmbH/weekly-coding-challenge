package com.rubengees.day5

object IntCodeExecutor {

    fun run(input: Program): ProgramExecutionResult {
        var outputs = emptyList<Int>()
        var instructionPointer = 0
        var program = input

        while (instructionPointer < program.length) {
            val opcode = OpcodeParser.parse(program[instructionPointer])
            val args = ArgumentParser.parse(program, opcode.argumentCount, instructionPointer)

            when (val result = opcode.execute(program, args)) {
                is Opcode.OpcodeResult.ProgramModification -> {
                    program = result.newProgram
                    instructionPointer += opcode.argumentCount + 1
                }
                is Opcode.OpcodeResult.InstructionPointerModification -> {
                    instructionPointer = result.newInstructionPointer
                }
                is Opcode.OpcodeResult.Output -> {
                    outputs = outputs + result.value
                    instructionPointer += opcode.argumentCount + 1
                }
                is Opcode.OpcodeResult.HaltProgram -> {
                    return ProgramExecutionResult(program, outputs)
                }
            }
        }

        error("Program exited without HALT opcode.")
    }

    class ProgramExecutionResult(val program: Program, val outputs: List<Int>)
}
