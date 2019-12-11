package com.rubengees.day7

import org.amshove.kluent.shouldEqual
import org.junit.jupiter.api.Test

class UtilsTest {

    @Test
    fun `generating the digits of 12345 should return a correct sequence`() {
        12345.digits().toList() shouldEqual listOf(5, 4, 3, 2, 1)
    }

    @Test
    fun `generating the digits of 1 should return a correct sequence`() {
        1.digits().toList() shouldEqual listOf(1)
    }

    @Test
    fun `merging the digits of 5, 4, 3, 2, 1 should return the correct result`() {
        listOf(5, 4, 3, 2, 1).mergeDigits() shouldEqual 12345
    }

    @Test
    fun `merging the digits of 1 should return the correct result`() {
        listOf(1).mergeDigits() shouldEqual 1
    }

    @Test
    fun `merging the digits of an empty list should return 0`() {
        emptyList<Int>().mergeDigits() shouldEqual 0
    }

    @Test
    fun `merging the digits of 5, 4, 3, 2, 1 as a sequence should return the correct result`() {
        sequenceOf(5, 4, 3, 2, 1).mergeDigits() shouldEqual 12345
    }
}
