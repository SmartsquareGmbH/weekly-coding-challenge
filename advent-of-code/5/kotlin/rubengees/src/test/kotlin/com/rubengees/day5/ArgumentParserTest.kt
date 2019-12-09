package com.rubengees.day5

import org.amshove.kluent.shouldEqual
import org.junit.jupiter.api.Test

class ArgumentParserTest {

    @Test
    fun `should resolve arguments correctly`() {
        val input = "1,9,10,3,2,3,11,0,99,30,40,50"
        val program = Program.parse(input)

        val expected = listOf(
            Opcode.Argument(9, Opcode.Argument.ArgumentMode.POSITION),
            Opcode.Argument(10, Opcode.Argument.ArgumentMode.POSITION),
            Opcode.Argument(3, Opcode.Argument.ArgumentMode.POSITION)
        )

        ArgumentParser.parse(program, 3, 0) shouldEqual expected
    }

    @Test
    fun `should handle argument modes`() {
        val input = "1001,9,10,3,2,3,11,0,99,30,40,50"
        val program = Program.parse(input)

        val expected = listOf(
            Opcode.Argument(9, Opcode.Argument.ArgumentMode.POSITION),
            Opcode.Argument(10, Opcode.Argument.ArgumentMode.IMMEDIATE),
            Opcode.Argument(3, Opcode.Argument.ArgumentMode.POSITION)
        )

        ArgumentParser.parse(program, 3, 0) shouldEqual expected
    }
}
