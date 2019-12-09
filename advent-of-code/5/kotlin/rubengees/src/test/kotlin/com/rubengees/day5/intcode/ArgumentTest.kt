package com.rubengees.day5.intcode

import org.amshove.kluent.shouldEqual
import org.junit.jupiter.api.Test

class ArgumentTest {

    @Test
    fun `should resolve arguments correctly`() {
        val input = "1,9,10,3,2,3,11,0,99,30,40,50"
        val program = Program.parse(input)

        val expected = listOf(
            Argument(9, Argument.ArgumentMode.POSITION),
            Argument(10, Argument.ArgumentMode.POSITION),
            Argument(3, Argument.ArgumentMode.POSITION)
        )

        Argument.parse(program, 3, 0) shouldEqual expected
    }

    @Test
    fun `should handle argument modes`() {
        val input = "1001,9,10,3,2,3,11,0,99,30,40,50"
        val program = Program.parse(input)

        val expected = listOf(
            Argument(9, Argument.ArgumentMode.POSITION),
            Argument(10, Argument.ArgumentMode.IMMEDIATE),
            Argument(3, Argument.ArgumentMode.POSITION)
        )

        Argument.parse(program, 3, 0) shouldEqual expected
    }
}
