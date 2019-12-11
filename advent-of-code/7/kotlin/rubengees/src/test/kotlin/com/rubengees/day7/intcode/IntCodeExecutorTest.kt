package com.rubengees.day7.intcode

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
    fun `running a program with multiple inputs should work`() {
        val program = Program.parse("3,15,3,16,1002,16,10,16,1,16,15,15,4,15,99,0,0").withInputs(1, 2)
        val result = IntCodeExecutor.run(program)

        result.outputs shouldEqual listOf(21)
    }

    @Test
    fun `running a program with multiple outputs should work`() {
        val program = Program.parse("4,0,4,1,4,6,99")
        val result = IntCodeExecutor.run(program)

        result.program shouldEqual Program.parse("4,0,4,1,4,6,99")
        result.outputs shouldEqual listOf(4, 0, 99)
    }

    @ParameterizedTest
    @CsvSource(
        value = [
            "3,9,8,9,10,9,4,9,99,-1,8;8;1",
            "3,9,8,9,10,9,4,9,99,-1,8;7;0",
            "3,9,8,9,10,9,4,9,99,-1,8;9;0",
            "3,9,7,9,10,9,4,9,99,-1,8;7;1",
            "3,9,7,9,10,9,4,9,99,-1,8;8;0",
            "3,9,7,9,10,9,4,9,99,-1,8;9;0",
            "3,3,1108,-1,8,3,4,3,99;8;1",
            "3,3,1108,-1,8,3,4,3,99;7;0",
            "3,3,1108,-1,8,3,4,3,99;9;0",
            "3,3,1107,-1,8,3,4,3,99;7;1",
            "3,3,1107,-1,8,3,4,3,99;8;0",
            "3,3,1107,-1,8,3,4,3,99;9;0"
        ],
        delimiter = ';'
    )
    fun `running conditional programs should work`(inputProgram: String, input: Int, expectedOutput: Int) {
        val program = Program.parse(inputProgram).withInput(input)
        val result = IntCodeExecutor.run(program)

        result.outputs shouldEqual listOf(expectedOutput)
    }

    @ParameterizedTest
    @CsvSource(
        value = [
            "3,12,6,12,15,1,13,14,13,4,13,99,-1,0,1,9;0;0",
            "3,12,6,12,15,1,13,14,13,4,13,99,-1,0,1,9;1;1",
            "3,3,1105,-1,9,1101,0,0,12,4,12,99,1;0;0",
            "3,3,1105,-1,9,1101,0,0,12,4,12,99,1;1;1"
        ],
        delimiter = ';'
    )
    fun `running jumping programs should work`(inputProgram: String, input: Int, expectedOutput: Int) {
        val program = Program.parse(inputProgram).withInput(input)
        val result = IntCodeExecutor.run(program)

        result.outputs shouldEqual listOf(expectedOutput)
    }

    @ParameterizedTest
    @CsvSource(value = ["7,999", "8,1000", "9,1001"])
    fun `running a complex program should work`(input: Int, expectedOutput: Int) {
        val rawProgram = """
            3,21,1008,21,8,20,1005,20,22,107,8,21,20,1006,20,31,
            1106,0,36,98,0,0,1002,21,125,20,4,20,1105,1,46,104,
            999,1105,1,46,1101,1000,1,20,4,20,1105,1,46,98,99
        """.trimIndent()

        val program = Program.parse(rawProgram).withInput(input)
        val result = IntCodeExecutor.run(program)

        result.outputs shouldEqual listOf(expectedOutput)
    }
}
