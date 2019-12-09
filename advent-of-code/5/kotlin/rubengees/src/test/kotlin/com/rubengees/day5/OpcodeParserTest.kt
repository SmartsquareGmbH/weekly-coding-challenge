package com.rubengees.day5

import org.amshove.kluent.shouldBeInstanceOf
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import kotlin.reflect.KClass

class OpcodeParserTest {

    companion object {
        @Suppress("unused")
        @JvmStatic
        fun `parsing an opcode should return the correct implementation`(): List<TestRow> {
            return listOf(
                TestRow(1, Opcode.Add::class),
                TestRow(2, Opcode.Multiply::class),
                TestRow(99, Opcode.Halt::class)
            )
        }
    }

    @ParameterizedTest(name = "{0} should result in {1}")
    @MethodSource
    fun `parsing an opcode should return the correct implementation`(row: TestRow) {
        OpcodeParser.parse(row.input) shouldBeInstanceOf row.output
    }

    data class TestRow(val input: Int, val output: KClass<out Opcode<Opcode.OpcodeResult>>)
}
