package com.rubengees.day9.intcode

import com.rubengees.day9.intcode.Argument.ArgumentMode.IMMEDIATE
import com.rubengees.day9.intcode.Argument.ArgumentMode.POSITION
import com.rubengees.day9.intcode.Opcode.Add
import com.rubengees.day9.intcode.Opcode.AdjustRelativeBase
import com.rubengees.day9.intcode.Opcode.Equals
import com.rubengees.day9.intcode.Opcode.Halt
import com.rubengees.day9.intcode.Opcode.JumpIfFalse
import com.rubengees.day9.intcode.Opcode.JumpIfTrue
import com.rubengees.day9.intcode.Opcode.LessThan
import com.rubengees.day9.intcode.Opcode.Multiply
import com.rubengees.day9.intcode.Opcode.OpcodeResult
import com.rubengees.day9.intcode.Opcode.ReadInput
import com.rubengees.day9.intcode.Opcode.WriteOutput
import org.amshove.kluent.shouldBeInstanceOf
import org.amshove.kluent.shouldBeNull
import org.amshove.kluent.shouldEqual
import org.amshove.kluent.shouldNotBeNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import kotlin.reflect.KClass

class OpcodeTest {

    companion object {
        @Suppress("unused")
        @JvmStatic
        fun `parsing an opcode should return the correct implementation`(): List<TestRow> {
            return listOf(
                TestRow(1L, Add::class),
                TestRow(2L, Multiply::class),
                TestRow(3L, ReadInput::class),
                TestRow(4L, WriteOutput::class),
                TestRow(5L, JumpIfTrue::class),
                TestRow(6L, JumpIfFalse::class),
                TestRow(7L, LessThan::class),
                TestRow(8L, Equals::class),
                TestRow(9L, AdjustRelativeBase::class),
                TestRow(99L, Halt::class)
            )
        }
    }

    @ParameterizedTest(name = "{0} should result in {1}")
    @MethodSource
    fun `parsing an opcode should return the correct implementation`(row: TestRow) {
        Opcode.parse(row.input) shouldBeInstanceOf row.output
    }

    data class TestRow(val input: Long, val output: KClass<out Opcode<OpcodeResult>>)

    @Test
    fun `Add should add the inputs and write to the output`() {
        val program = Program.parse("1,5,6,7,99,100,100,0")
        val args = listOf(Argument(5L, POSITION), Argument(6L, POSITION), Argument(7L, POSITION))

        val result = Add.execute(program, args)

        result.newProgram[7L] shouldEqual 200L
    }

    @Test
    fun `Multiply should multiply the inputs and write to the output`() {
        val program = Program.parse("1,5,6,7,99,100,100,0")
        val args = listOf(Argument(5L, POSITION), Argument(6L, POSITION), Argument(7L, POSITION))

        val result = Multiply.execute(program, args)

        result.newProgram[7L] shouldEqual 10000L
    }

    @Test
    fun `reading input should write it to the output position`() {
        val program = Program.parse("1,5,6,7,99,100,100,0").withInput(999L)
        val args = listOf(Argument(5L, POSITION))

        val result = ReadInput.execute(program, args)

        result.newProgram[5L] shouldEqual 999L
    }

    @Test
    fun `reading input for a program with multiple inputs should shift the input`() {
        val program = Program.parse("3,15,3,16,1002,16,10,16,1,16,15,15,4,15,99,0,0").withInputs(1L, 2L)
        val args = listOf(Argument(15L, POSITION))

        val result = ReadInput.execute(program, args)

        result.newProgram[15L] shouldEqual 1L
        result.newProgram.input shouldEqual 2L
    }

    @Test
    fun `writing output should return correct result`() {
        val program = Program.parse("1,5,6,7,99,100,100,0")
        val args = listOf(Argument(4L, POSITION))

        val result = WriteOutput.execute(program, args)

        result.value shouldEqual 99L
    }

    @Test
    fun `jumping if non zero should work`() {
        val args = listOf(Argument(12L, IMMEDIATE), Argument(33L, IMMEDIATE))

        val result = JumpIfTrue.execute(Program.parse("99"), args)

        result.shouldNotBeNull()
        result.newInstructionPointer shouldEqual 33L
    }

    @Test
    fun `jumping if non zero should not jump if zero`() {
        val args = listOf(Argument(0L, IMMEDIATE), Argument(33L, IMMEDIATE))

        val result = JumpIfTrue.execute(Program.parse("99"), args)

        result.shouldBeNull()
    }

    @Test
    fun `jumping if zero should work`() {
        val args = listOf(Argument(0L, IMMEDIATE), Argument(13L, IMMEDIATE))

        val result = JumpIfFalse.execute(Program.parse("99"), args)

        result.shouldNotBeNull()
        result.newInstructionPointer shouldEqual 13L
    }

    @Test
    fun `jumping if zero should not jump if non zero`() {
        val args = listOf(Argument(1L, IMMEDIATE), Argument(13L, IMMEDIATE))

        val result = JumpIfFalse.execute(Program.parse("99"), args)

        result.shouldBeNull()
    }

    @Test
    fun `comparing with less than should write the correct value if less`() {
        val program = Program.parse("7,5,6,7,99,100,101,999")
        val args = listOf(Argument(5L, POSITION), Argument(6L, POSITION), Argument(7L, IMMEDIATE))

        val result = LessThan.execute(program, args)

        result.newProgram[7L] shouldEqual 1L
    }

    @Test
    fun `comparing with less than should write the correct value if not less`() {
        val program = Program.parse("7,5,6,7,99,101,100,999")
        val args = listOf(Argument(5L, POSITION), Argument(6L, POSITION), Argument(7L, IMMEDIATE))

        val result = LessThan.execute(program, args)

        result.newProgram[7L] shouldEqual 0L
    }

    @Test
    fun `comparing with equals should write the correct value if equal`() {
        val program = Program.parse("8,5,6,7,99,100,100,999")
        val args = listOf(Argument(5L, POSITION), Argument(6L, POSITION), Argument(7L, IMMEDIATE))

        val result = Equals.execute(program, args)

        result.newProgram[7L] shouldEqual 1L
    }

    @Test
    fun `comparing with equals should write the correct value if not equal`() {
        val program = Program.parse("8,5,6,7,99,100,300,999")
        val args = listOf(Argument(5L, POSITION), Argument(6L, POSITION), Argument(7L, IMMEDIATE))

        val result = Equals.execute(program, args)

        result.newProgram[7L] shouldEqual 0L
    }

    @Test
    fun `adjusting the relative base should work`() {
        val program = Program.parse("99")
        val args1 = listOf(Argument(-3, IMMEDIATE))
        val args2 = listOf(Argument(0, POSITION))

        val result1 = AdjustRelativeBase.execute(program, args1)
        val result2 = AdjustRelativeBase.execute(result1.newProgram, args2)

        result1.newProgram.relativeBase shouldEqual -3
        result2.newProgram.relativeBase shouldEqual 96
    }
}
