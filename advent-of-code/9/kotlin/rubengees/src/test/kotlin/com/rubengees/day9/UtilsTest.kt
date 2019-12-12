package com.rubengees.day9

import org.amshove.kluent.shouldEqual
import org.junit.jupiter.api.Test

class UtilsTest {

    @Test
    fun `generating the digits of 12345 should return a correct sequence`() {
        12345L.digits().toList() shouldEqual listOf(5L, 4L, 3L, 2L, 1L)
    }

    @Test
    fun `generating the digits of 1 should return a correct sequence`() {
        1L.digits().toList() shouldEqual listOf(1L)
    }

    @Test
    fun `merging the digits of 5, 4, 3, 2, 1 should return the correct result`() {
        listOf(5L, 4L, 3L, 2L, 1L).mergeDigits() shouldEqual 12345L
    }

    @Test
    fun `merging the digits of 1 should return the correct result`() {
        listOf(1L).mergeDigits() shouldEqual 1L
    }

    @Test
    fun `merging the digits of an empty list should return 0`() {
        emptyList<Long>().mergeDigits() shouldEqual 0L
    }

    @Test
    fun `merging the digits of 5, 4, 3, 2, 1 as a sequence should return the correct result`() {
        sequenceOf(5L, 4L, 3L, 2L, 1L).mergeDigits() shouldEqual 12345L
    }
}
