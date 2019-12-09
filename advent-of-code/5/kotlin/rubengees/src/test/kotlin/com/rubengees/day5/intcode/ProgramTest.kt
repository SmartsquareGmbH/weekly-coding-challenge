package com.rubengees.day5.intcode

import org.amshove.kluent.shouldEqual
import org.junit.jupiter.api.Test

class ProgramTest {

    @Test
    fun `parse should construct a correct program`() {
        val input = "1,9,10,3,2,3,11,0,99,30,40,50"
        val result = Program.parse(input)

        result[0] shouldEqual 1
        result[2] shouldEqual 10
        result[11] shouldEqual 50
    }

    @Test
    fun `update should work`() {
        val input = "1,9,10,3,2,3,11,0,99,30,40,50"
        val program = Program.parse(input)
        val result = program.update(4, 100)

        program[4] shouldEqual 2
        result[4] shouldEqual 100
    }
}
