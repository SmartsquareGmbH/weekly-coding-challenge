package com.rubengees.day5.intcode

import org.amshove.kluent.shouldEqual
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class IntCodeExecutorTest {

    @ParameterizedTest
    @CsvSource(
        value = [
            "1,9,10,3,2,3,11,0,99,30,40,50;3500,9,10,70,2,3,11,0,99,30,40,50",
            "1,0,0,0,99;2,0,0,0,99",
            "2,3,0,3,99;2,3,0,6,99",
            "2,4,4,5,99,0;2,4,4,5,99,9801",
            "1,1,1,4,99,5,6,0,99;30,1,1,4,2,5,6,0,99"
        ],
        delimiter = ';'
    )
    fun `running simple programs should work`(input: String, expected: String) {
        val program = Program.parse(input)
        val result = IntCodeExecutor.run(program)

        result.program shouldEqual Program.parse(expected)
    }

    @Test
    fun `running programs with argument modes should work`() {
        val program = Program.parse("1002,4,3,4,33")
        val result = IntCodeExecutor.run(program)

        result.program shouldEqual Program.parse("1002,4,3,4,99")
    }

    @Test
    fun `running programs with negative numbers should work`() {
        val program = Program.parse("1101,100,-1,4,0")
        val result = IntCodeExecutor.run(program)

        result.program shouldEqual Program.parse("1101,100,-1,4,99")
    }

    @Test
    fun `running programs with input and output should work`() {
        val program = Program.parse("3,0,4,0,99").withInput(999)
        val result = IntCodeExecutor.run(program)

        result.program shouldEqual Program.parse("999,0,4,0,99")
        result.outputs shouldEqual listOf(999)
    }

    @Test
    fun `running a program with multiple outputs should work`() {
        val program = Program.parse("4,0,4,1,4,6,99")
        val result = IntCodeExecutor.run(program)

        result.program shouldEqual Program.parse("4,0,4,1,4,6,99")
        result.outputs shouldEqual listOf(4, 0, 99)
    }
}
