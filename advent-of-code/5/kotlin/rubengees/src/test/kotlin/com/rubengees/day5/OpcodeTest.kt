package com.rubengees.day5

import com.rubengees.day5.Opcode.Add
import com.rubengees.day5.Opcode.Argument
import com.rubengees.day5.Opcode.Argument.ArgumentMode.IMMEDIATE
import com.rubengees.day5.Opcode.Argument.ArgumentMode.POSITION
import com.rubengees.day5.Opcode.Multiply
import com.rubengees.day5.Opcode.ReadInput
import com.rubengees.day5.Opcode.WriteOutput
import org.amshove.kluent.shouldEqual
import org.junit.jupiter.api.Test

class OpcodeTest {

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
