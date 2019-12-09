package com.rubengees.day5

import com.rubengees.day5.Opcode.OpcodeResult.HaltProgram
import com.rubengees.day5.Opcode.OpcodeResult.Output
import com.rubengees.day5.Opcode.OpcodeResult.ProgramModification

sealed class Opcode<out O : Opcode.OpcodeResult>(val argumentCount: Int) {

    abstract fun execute(program: Program, args: List<Argument>): O

    object Add : Opcode<ProgramModification>(3) {

        override fun execute(program: Program, args: List<Argument>): ProgramModification {
            val (input1, input2, output) = args
            val newProgram = program.update(output.value, input1.resolve(program) + input2.resolve(program))

            return ProgramModification(newProgram)
        }
    }

    object Multiply : Opcode<ProgramModification>(3) {

        override fun execute(program: Program, args: List<Argument>): ProgramModification {
            val (input1, input2, output) = args
            val newProgram = program.update(output.value, input1.resolve(program) * input2.resolve(program))

            return ProgramModification(newProgram)
        }
    }

    object ReadInput : Opcode<ProgramModification>(1) {

        override fun execute(program: Program, args: List<Argument>): ProgramModification {
            val (output) = args
            val newProgram = program.update(output.value, program.input)

            return ProgramModification(newProgram)
        }
    }

    object WriteOutput : Opcode<Output>(1) {

        override fun execute(program: Program, args: List<Argument>): Output {
            val (inputPosition) = args

            return Output(inputPosition.resolve(program))
        }
    }

    object Halt : Opcode<HaltProgram>(0) {
        override fun execute(program: Program, args: List<Argument>) = HaltProgram
    }

    data class Argument(val value: Int, val mode: ArgumentMode) {

        fun resolve(program: Program) = when (mode) {
            ArgumentMode.POSITION -> program[value]
            ArgumentMode.IMMEDIATE -> value
        }

        enum class ArgumentMode { POSITION, IMMEDIATE }
    }

    sealed class OpcodeResult {
        data class ProgramModification(val newProgram: Program) : OpcodeResult()
        data class InstructionPointerModification(val newInstructionPointer: Int) : OpcodeResult()
        data class Output(val value: Int) : OpcodeResult()
        object HaltProgram : OpcodeResult()
    }
}
