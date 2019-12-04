package com.github.deen13.four

import io.kotlintest.data.forall
import io.kotlintest.shouldBe
import io.kotlintest.specs.FeatureSpec
import io.kotlintest.tables.row

internal class PasswordFinderSpecification : FeatureSpec({

    feature("isSixDigitsLong") {
        scenario("values are six digits long") {
            forall(row(100_000), row(999_999)) { value ->
                value.isSixDigitsLong() shouldBe true
            }
        }

        scenario("values is shorter than six digits") {
            99_999.isSixDigitsLong() shouldBe false

        }

        scenario("values is longer than six digits") {
            1_000_000.isSixDigitsLong() shouldBe false
        }
    }

    feature("hasAdjacentDigits") {
        scenario("111111") {
            111111.hasAdjacentDigits() shouldBe true
        }

        scenario("135679") {
            135679.hasAdjacentDigits() shouldBe false
        }

        scenario("122345") {
            122345.hasAdjacentDigits() shouldBe true
        }
    }

    feature("hasNoDecreasingPairOfDigits") {
        scenario("223450") {
            223450.hasNoDecreasingPairOfDigits() shouldBe false
        }

        scenario("111111") {
            111111.hasNoDecreasingPairOfDigits() shouldBe true
        }

        scenario("123789") {
            123789.hasNoDecreasingPairOfDigits() shouldBe true
        }
    }

    feature("findPasswordsInRange - Part 1") {

        val filters = listOf(Int::hasNoDecreasingPairOfDigits, Int::hasAdjacentDigits, Int::isSixDigitsLong)

        scenario("finds one password") {
            findPasswordsInRange(357_253..357_777, filters).size shouldBe 1
        }

        scenario("finds passwords from aoc input") {
            findPasswordsInRange(23_5741..70_6948, filters).size shouldBe 1178
        }
    }

    feature("hasOnePairOfAdjacentDigits") {

        scenario("112233") {
            112233.hasOnePairOfAdjacentDigits() shouldBe true
        }

        scenario("123444") {
            123444.hasOnePairOfAdjacentDigits() shouldBe false
        }

        scenario("111122") {
            111122.hasOnePairOfAdjacentDigits() shouldBe true
        }
    }

    feature("findPasswordsInRange - Part 2") {

        val filters = listOf(Int::hasNoDecreasingPairOfDigits, Int::hasOnePairOfAdjacentDigits, Int::isSixDigitsLong)

        scenario("finds passwords from aoc input") {
            findPasswordsInRange(235_741..706_948, filters).size shouldBe 763
        }
    }
})
