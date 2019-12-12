package com.rubengees.day9.intcode

import com.rubengees.day9.intcode.Argument.ArgumentMode.IMMEDIATE
import com.rubengees.day9.intcode.Argument.ArgumentMode.POSITION
import com.rubengees.day9.intcode.Argument.ArgumentMode.RELATIVE
import org.amshove.kluent.shouldEqual
import org.junit.jupiter.api.Test

class ArgumentTest {

    @Test
    fun `should resolve arguments correctly`() {
        val input = "1,9,10,3,2,3,11,0,99,30,40,50"
        val program = Program.parse(input)

        val expected = listOf(
            Argument(9L, POSITION),
            Argument(10L, POSITION),
            Argument(3L, POSITION)
        )

        Argument.parse(program, 3, 0L) shouldEqual expected
    }

    @Test
    fun `should handle argument modes`() {
        val input = "21001,9,10,3,2,3,11,0,99,30,40,50"
        val program = Program.parse(input)

        val expected = listOf(
            Argument(9L, POSITION),
            Argument(10L, IMMEDIATE),
            Argument(3L, RELATIVE)
        )

        Argument.parse(program, 3, 0L) shouldEqual expected
    }

    @Test
    fun `resolving a positional argument should work`() {
        val program = Program.parse("1,5,6,7,99,100,100,0")

        Argument(5L, POSITION).resolve(program) shouldEqual 100L
    }

    @Test
    fun `resolving an immediate argument should work`() {
        val program = Program.parse("1,5,6,7,99,100,100,0")

        Argument(5L, IMMEDIATE).resolve(program) shouldEqual 5L
    }

    @Test
    fun `resolving an relative argument should work`() {
        val program = Program.parse("1,5,6,7,99,100,100,0").adjustRelativeBase(-2)

        Argument(6L, RELATIVE).resolve(program) shouldEqual 99L
    }

    @Test
    fun `resolving a positional output argument should work`() {
        val program = Program.parse("1,5,6,7,99,100,100,0")

        Argument(5L, POSITION).resolveOutput(program) shouldEqual 5L
    }

    @Test
    fun `resolving an immediate output argument should work`() {
        val program = Program.parse("1,5,6,7,99,100,100,0")

        Argument(5L, IMMEDIATE).resolveOutput(program) shouldEqual 5L
    }

    @Test
    fun `resolving an relative output argument should work`() {
        val program = Program.parse("1,5,6,7,99,100,100,0").adjustRelativeBase(-2)

        Argument(6L, RELATIVE).resolveOutput(program) shouldEqual 4L
    }
}
