package com.rubengees.day6

import org.amshove.kluent.shouldBeNull
import org.amshove.kluent.shouldEqual
import org.amshove.kluent.shouldHaveSize
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class AppTest {

    @Test
    fun `parse should return correct map`() {
        val input = listOf("COM)B", "B)C", "C)D", "D)E", "E)F", "B)G", "G)H", "D)I", "E)J", "J)K", "K)L")
        val result = parse(input)

        result.shouldHaveSize(11)
        result["COM"].shouldBeNull()
        result["B"] shouldEqual "COM"
        result["G"] shouldEqual "B"
        result["L"] shouldEqual "K"
    }

    @ParameterizedTest
    @CsvSource("D, 3", "L, 7", "COM, 0")
    fun `calculating orbit amount should return correct result`(planet: String, orbits: Int) {
        val input = listOf("COM)B", "B)C", "C)D", "D)E", "E)F", "B)G", "G)H", "D)I", "E)J", "J)K", "K)L")
        val map = parse(input)

        calculateOrbits(map, planet) shouldEqual orbits
    }

    @Test
    fun `calculating all orbits should return correct result`() {
        val input = listOf("COM)B", "B)C", "C)D", "D)E", "E)F", "B)G", "G)H", "D)I", "E)J", "J)K", "K)L")
        val map = parse(input)

        calculateAllOrbits(map) shouldEqual 42
    }

    @Test
    fun `finding intersection between two planets should return correct result`() {
        val input = listOf(
            "COM)B", "B)C", "C)D", "D)E", "E)F", "B)G", "G)H", "D)I", "E)J", "J)K", "K)L", "K)YOU", "I)SAN"
        )

        val map = parse(input)

        findIntersection(map, "YOU", "SAN") shouldEqual "D"
    }

    @Test
    fun `calculating shortest way should return correct result`() {
        val input = listOf(
            "COM)B", "B)C", "C)D", "D)E", "E)F", "B)G", "G)H", "D)I", "E)J", "J)K", "K)L", "K)YOU", "I)SAN"
        )

        val map = parse(input)

        calculateShortestWay(map, "YOU", "SAN") shouldEqual 4
    }
}
