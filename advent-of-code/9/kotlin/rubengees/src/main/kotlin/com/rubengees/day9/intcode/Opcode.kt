package com.rubengees.day9.intcode

import com.rubengees.day9.digits
import com.rubengees.day9.intcode.Opcode.OpcodeResult.HaltProgram
import com.rubengees.day9.intcode.Opcode.OpcodeResult.InstructionPointerModification
import com.rubengees.day9.intcode.Opcode.OpcodeResult.Output
import com.rubengees.day9.intcode.Opcode.OpcodeResult.ProgramModification
import com.rubengees.day9.mergeDigits

sealed class Opcode<out O : Opcode.OpcodeResult>(val argumentCount: Int) {

    companion object {
        fun parse(input: Long): Opcode<OpcodeResult> {
            return when (val code = input.digits().take(2).mergeDigits()) {
                1L -> Add
                2L -> Multiply
                3L -> ReadInput
                4L -> WriteOutput
                5L -> JumpIfTrue
                6L -> JumpIfFalse
                7L -> LessThan
                8L -> Equals
                9L -> AdjustRelativeBase
                99L -> Halt
                else -> error("Unknown opcode: $code")
            }
        }
    }

    abstract fun execute(program: Program, args: List<Argument>): O?

    object Add : Opcode<ProgramModification>(3) {

        override fun execute(program: Program, args: List<Argument>): ProgramModification {
            val (input1, input2, output) = args

            val newProgram = program.update(
                output.resolveOutput(program),
                input1.resolve(program) + input2.resolve(program)
            )

            return ProgramModification(newProgram)
        }
    }

    object Multiply : Opcode<ProgramModification>(3) {

        override fun execute(program: Program, args: List<Argument>): ProgramModification {
            val (input1, input2, output) = args

            val newProgram = program.update(
                output.resolveOutput(program),
                input1.resolve(program) * input2.resolve(program)
            )

            return ProgramModification(newProgram)
        }
    }

    object ReadInput : Opcode<ProgramModification>(1) {

        override fun execute(program: Program, args: List<Argument>): ProgramModification {
            val (output) = args
            val newProgram = program.update(output.resolveOutput(program), requireNotNull(program.input)).shiftInputs()

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
                0L -> null
                else -> InstructionPointerModification(output.resolve(program))
            }
        }
    }

    object JumpIfFalse : Opcode<InstructionPointerModification>(2) {

        override fun execute(program: Program, args: List<Argument>): InstructionPointerModification? {
            val (input, output) = args

            return when (input.resolve(program)) {
                0L -> InstructionPointerModification(output.resolve(program))
                else -> null
            }
        }
    }

    object LessThan : Opcode<ProgramModification>(3) {

        override fun execute(program: Program, args: List<Argument>): ProgramModification {
            val (input1, input2, output) = args

            val newProgram = when (input1.resolve(program) < input2.resolve(program)) {
                true -> program.update(output.resolveOutput(program), 1)
                false -> program.update(output.resolveOutput(program), 0)
            }

            return ProgramModification(newProgram)
        }
    }

    object Equals : Opcode<ProgramModification>(3) {

        override fun execute(program: Program, args: List<Argument>): ProgramModification {
            val (input1, input2, output) = args

            val newProgram = when (input1.resolve(program) == input2.resolve(program)) {
                true -> program.update(output.resolveOutput(program), 1)
                false -> program.update(output.resolveOutput(program), 0)
            }

            return ProgramModification(newProgram)
        }
    }

    object AdjustRelativeBase : Opcode<ProgramModification>(1) {

        override fun execute(program: Program, args: List<Argument>): ProgramModification {
            val (input) = args

            return ProgramModification(program.adjustRelativeBase(input.resolve(program)))
        }
    }

    object Halt : Opcode<HaltProgram>(0) {
        override fun execute(program: Program, args: List<Argument>) = HaltProgram
    }

    sealed class OpcodeResult {
        data class ProgramModification(val newProgram: Program) : OpcodeResult()
        data class InstructionPointerModification(val newInstructionPointer: Long) : OpcodeResult()
        data class Output(val value: Long) : OpcodeResult()
        object HaltProgram : OpcodeResult()
    }
}
