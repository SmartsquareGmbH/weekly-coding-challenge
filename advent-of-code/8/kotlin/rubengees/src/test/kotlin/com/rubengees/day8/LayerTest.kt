package com.rubengees.day8

import org.amshove.kluent.shouldEqual
import org.junit.jupiter.api.Test

class LayerTest {

    @Test
    fun `should return correct number of digits`() {
        val layer = Layer(
            listOf(
                listOf(1, 2, 3),
                listOf(3, 2, 3),
                listOf(0, 2, 3)
            )
        )

        layer.numberOfDigits(0) shouldEqual 1
        layer.numberOfDigits(1) shouldEqual 1
        layer.numberOfDigits(2) shouldEqual 3
        layer.numberOfDigits(3) shouldEqual 4
        layer.numberOfDigits(4) shouldEqual 0
    }

    @Test
    fun `merging two layers should return correct layer`() {
        val layer1 = Layer(
            listOf(
                listOf(0, 0),
                listOf(1, 2)
            )
        )

        val layer2 = Layer(
            listOf(
                listOf(1, 2),
                listOf(2, 0)
            )
        )

        val expected = Layer(
            listOf(
                listOf(0, 0),
                listOf(1, 0)
            )
        )

        layer1.mergeWith(layer2) shouldEqual expected
    }

    @Test
    fun `merging multiple layers return correct layer`() {
        val layer1 = Layer(
            listOf(
                listOf(0, 2),
                listOf(2, 2)
            )
        )

        val layer2 = Layer(
            listOf(
                listOf(1, 1),
                listOf(2, 2)
            )
        )

        val layer3 = Layer(
            listOf(
                listOf(2, 2),
                listOf(1, 2)
            )
        )

        val layer4 = Layer(
            listOf(
                listOf(0, 0),
                listOf(0, 0)
            )
        )

        val expected = Layer(
            listOf(
                listOf(0, 1),
                listOf(1, 0)
            )
        )

        layer1.mergeWith(layer2).mergeWith(layer3).mergeWith(layer4) shouldEqual expected
    }
}
