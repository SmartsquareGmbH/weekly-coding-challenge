package com.rubengees.day5.intcode

import com.rubengees.day5.digits
import com.rubengees.day5.intcode.Opcode.OpcodeResult.HaltProgram
import com.rubengees.day5.intcode.Opcode.OpcodeResult.Output
import com.rubengees.day5.intcode.Opcode.OpcodeResult.ProgramModification
import com.rubengees.day5.mergeDigits

sealed class Opcode<out O : Opcode.OpcodeResult>(val argumentCount: Int) {

    companion object {
        fun parse(input: Int): Opcode<OpcodeResult> {
            return when (val code = input.digits().take(2).mergeDigits()) {
                1 -> Add
                2 -> Multiply
                3 -> ReadInput
                4 -> WriteOutput
                99 -> Halt
                else -> error("Unknown opcode: $code")
            }
        }
    }

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

    sealed class OpcodeResult {
        data class ProgramModification(val newProgram: Program) : OpcodeResult()
        data class InstructionPointerModification(val newInstructionPointer: Int) : OpcodeResult()
        data class Output(val value: Int) : OpcodeResult()
        object HaltProgram : OpcodeResult()
    }
}
