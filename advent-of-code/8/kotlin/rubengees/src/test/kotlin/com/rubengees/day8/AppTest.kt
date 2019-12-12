package com.rubengees.day8

import org.amshove.kluent.shouldEqual
import org.junit.jupiter.api.Test

class AppTest {

    @Test
    fun `parsing should return correct data structure`() {
        val parsed = parse("123456789012", 3, 2)

        parsed shouldEqual listOf(
            Layer(listOf(listOf(1, 2, 3), listOf(4, 5, 6))),
            Layer(listOf(listOf(7, 8, 9), listOf(0, 1, 2)))
        )
    }
}
