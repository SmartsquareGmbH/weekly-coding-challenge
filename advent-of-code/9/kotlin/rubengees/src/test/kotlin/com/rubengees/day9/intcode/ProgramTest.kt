package com.rubengees.day9.intcode

import org.amshove.kluent.shouldEqual
import org.junit.jupiter.api.Test

class ProgramTest {

    @Test
    fun `parse should construct a correct program`() {
        val result = Program.parse("1,9,10,3,2,3,11,0,99,30,40,50")

        result[0L] shouldEqual 1L
        result[2L] shouldEqual 10L
        result[11L] shouldEqual 50L
    }

    @Test
    fun `update should work`() {
        val program = Program.parse("1,9,10,3,2,3,11,0,99,30,40,50")
        val result = program.update(4L, 100L)

        program[4L] shouldEqual 2L
        result[4L] shouldEqual 100L
    }

    @Test
    fun `should be able to handle indices outside of initial memory`() {
        val program = Program.parse("1,9,10,3,2,3,11,0,99,30,40,50")
        val result = (program.update(1000000000L, 123L))

        result[1000000000L] shouldEqual 123L
        result.length shouldEqual 1000000001L
    }
}
