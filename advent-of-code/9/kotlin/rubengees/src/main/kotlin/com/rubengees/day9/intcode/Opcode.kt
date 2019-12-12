package com.rubengees.day9.intcode

import com.rubengees.day9.digits
import com.rubengees.day9.intcode.Opcode.OpcodeResult.HaltProgram
import com.rubengees.day9.intcode.Opcode.OpcodeResult.InstructionPointerModification
import com.rubengees.day9.intcode.Opcode.OpcodeResult.Output
import com.rubengees.day9.intcode.Opcode.OpcodeResult.ProgramModification
import com.rubengees.day9.mergeDigits

sealed class Opcode<out O : Opcode.OpcodeResult>(val argumentCount: Int) {

    companion object {
        fun parse(input: Int): Opcode<OpcodeResult> {
            return when (val code = input.digits().take(2).mergeDigits()) {
                1 -> Add
                2 -> Multiply
                3 -> ReadInput
                4 -> WriteOutput
                5 -> JumpIfTrue
                6 -> JumpIfFalse
                7 -> LessThan
                8 -> Equals
                99 -> Halt
                else -> error("Unknown opcode: $code")
            }
        }
    }

    abstract fun execute(program: Program, args: List<Argument>): O?

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
            val newProgram = program.update(output.value, requireNotNull(program.input)).shiftInputs()

            return ProgramModification(newProgram)
        }
    }

    object WriteOutput : Opcode<Output>(1) {

        override fun execute(program: Program, args: List<Argument>): Output {
            val (inputPosition) = args

            return Output(inputPosition.resolve(program))
        }
    }

    object JumpIfTrue : Opcode<InstructionPointerModification>(2) {

        override fun execute(program: Program, args: List<Argument>): InstructionPointerModification? {
            val (input, output) = args

            return when (input.resolve(program)) {
                0 -> null
                else -> InstructionPointerModification(output.resolve(program))
            }
        }
    }

    object JumpIfFalse : Opcode<InstructionPointerModification>(2) {

        override fun execute(program: Program, args: List<Argument>): InstructionPointerModification? {
            val (input, output) = args

            return when (input.resolve(program)) {
                0 -> InstructionPointerModification(output.resolve(program))
                else -> null
            }
        }
    }

    object LessThan : Opcode<ProgramModification>(3) {

        override fun execute(program: Program, args: List<Argument>): ProgramModification {
            val (input1, input2, output) = args

            val newProgram = when (input1.resolve(program) < input2.resolve(program)) {
                true -> program.update(output.value, 1)
                false -> program.update(output.value, 0)
            }

            return ProgramModification(newProgram)
        }
    }

    object Equals : Opcode<ProgramModification>(3) {

        override fun execute(program: Program, args: List<Argument>): ProgramModification {
            val (input1, input2, output) = args

            val newProgram = when (input1.resolve(program) == input2.resolve(program)) {
                true -> program.update(output.value, 1)
                false -> program.update(output.value, 0)
            }

            return ProgramModification(newProgram)
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
