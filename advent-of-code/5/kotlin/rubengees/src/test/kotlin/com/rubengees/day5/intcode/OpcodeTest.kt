package com.rubengees.day5.intcode

import com.rubengees.day5.intcode.Argument.ArgumentMode.IMMEDIATE
import com.rubengees.day5.intcode.Argument.ArgumentMode.POSITION
import com.rubengees.day5.intcode.Opcode.Add
import com.rubengees.day5.intcode.Opcode.Equals
import com.rubengees.day5.intcode.Opcode.Halt
import com.rubengees.day5.intcode.Opcode.JumpIfFalse
import com.rubengees.day5.intcode.Opcode.JumpIfTrue
import com.rubengees.day5.intcode.Opcode.LessThan
import com.rubengees.day5.intcode.Opcode.Multiply
import com.rubengees.day5.intcode.Opcode.ReadInput
import com.rubengees.day5.intcode.Opcode.WriteOutput
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
                TestRow(1, Add::class),
                TestRow(2, Multiply::class),
                TestRow(3, ReadInput::class),
                TestRow(4, WriteOutput::class),
                TestRow(5, JumpIfTrue::class),
                TestRow(6, JumpIfFalse::class),
                TestRow(7, LessThan::class),
                TestRow(8, Equals::class),
                TestRow(99, Halt::class)
            )
        }
    }

    @ParameterizedTest(name = "{0} should result in {1}")
    @MethodSource
    fun `parsing an opcode should return the correct implementation`(row: TestRow) {
        Opcode.parse(row.input) shouldBeInstanceOf row.output
    }

    data class TestRow(val input: Int, val output: KClass<out Opcode<Opcode.OpcodeResult>>)

    @Test
    fun `Add should add the inputs and write to the output`() {
        val program = Program.parse("1,5,6,7,99,100,100,0")
        val args = listOf(Argument(5, POSITION), Argument(6, POSITION), Argument(7, POSITION))

        val result = Add.execute(program, args)

        result.newProgram[7] shouldEqual 200
    }

    @Test
    fun `Multiply should multiply the inputs and write to the output`() {
        val program = Program.parse("1,5,6,7,99,100,100,0")
        val args = listOf(Argument(5, POSITION), Argument(6, POSITION), Argument(7, POSITION))

        val result = Multiply.execute(program, args)

        result.newProgram[7] shouldEqual 10000
    }

    @Test
    fun `reading input should write it to the output position`() {
        val program = Program.parse("1,5,6,7,99,100,100,0").withInput(999)
        val args = listOf(Argument(5, POSITION))

        val result = ReadInput.execute(program, args)

        result.newProgram[5] shouldEqual 999
    }

    @Test
    fun `writing output should return correct result`() {
        val program = Program.parse("1,5,6,7,99,100,100,0")
        val args = listOf(Argument(4, POSITION))

        val result = WriteOutput.execute(program, args)

        result.value shouldEqual 99
    }

    @Test
    fun `jumping if non zero should work`() {
        val args = listOf(Argument(12, IMMEDIATE), Argument(33, IMMEDIATE))

        val result = JumpIfTrue.execute(Program.parse("99"), args)

        result.shouldNotBeNull()
        result.newInstructionPointer shouldEqual 33
    }

    @Test
    fun `jumping if non zero should not jump if zero`() {
        val args = listOf(Argument(0, IMMEDIATE), Argument(33, IMMEDIATE))

        val result = JumpIfTrue.execute(Program.parse("99"), args)

        result.shouldBeNull()
    }

    @Test
    fun `jumping if zero should work`() {
        val args = listOf(Argument(0, IMMEDIATE), Argument(13, IMMEDIATE))

        val result = JumpIfFalse.execute(Program.parse("99"), args)

        result.shouldNotBeNull()
        result.newInstructionPointer shouldEqual 13
    }

    @Test
    fun `jumping if zero should not jump if non zero`() {
        val args = listOf(Argument(1, IMMEDIATE), Argument(13, IMMEDIATE))

        val result = JumpIfFalse.execute(Program.parse("99"), args)

        result.shouldBeNull()
    }

    @Test
    fun `comparing with less than should write the correct value if less`() {
        val program = Program.parse("7,5,6,7,99,100,101,999")
        val args = listOf(Argument(5, POSITION), Argument(6, POSITION), Argument(7, IMMEDIATE))

        val result = LessThan.execute(program, args)

        result.newProgram[7] shouldEqual 1
    }

    @Test
    fun `comparing with less than should write the correct value if not less`() {
        val program = Program.parse("7,5,6,7,99,101,100,999")
        val args = listOf(Argument(5, POSITION), Argument(6, POSITION), Argument(7, IMMEDIATE))

        val result = LessThan.execute(program, args)

        result.newProgram[7] shouldEqual 0
    }

    @Test
    fun `comparing with equals should write the correct value if equal`() {
        val program = Program.parse("8,5,6,7,99,100,100,999")
        val args = listOf(Argument(5, POSITION), Argument(6, POSITION), Argument(7, IMMEDIATE))

        val result = Equals.execute(program, args)

        result.newProgram[7] shouldEqual 1
    }

    @Test
    fun `comparing with equals should write the correct value if not equal`() {
        val program = Program.parse("8,5,6,7,99,100,300,999")
        val args = listOf(Argument(5, POSITION), Argument(6, POSITION), Argument(7, IMMEDIATE))

        val result = Equals.execute(program, args)

        result.newProgram[7] shouldEqual 0
    }

    @Test
    fun `resolving a positional argument should work`() {
        val program = Program.parse("1,5,6,7,99,100,100,0")

        Argument(5, POSITION).resolve(program) shouldEqual 100
    }

    @Test
    fun `resolving an immediate argument should work`() {
        val program = Program.parse("1,5,6,7,99,100,100,0")

        Argument(5, IMMEDIATE).resolve(program) shouldEqual 5
    }
}
