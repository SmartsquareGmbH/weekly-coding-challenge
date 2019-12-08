package com.rubengees.day6

import java.nio.file.Files
import java.nio.file.Paths

fun main() {
    val input = Files.readAllLines(Paths.get(object {}.javaClass.classLoader.getResource("input.txt")!!.toURI()))
    val orbitMap = parse(input)

    println(calculateAllOrbits(orbitMap))
}

fun parse(input: List<String>): Map<String, List<String>> {
    return input
        .map {
            val (first, second) = it.split(")")

            first to second
        }
        .groupingBy { it.second }
        .aggregate { _, accumulator, element, _ -> accumulator?.plus(element.first) ?: listOf(element.first) }
}

fun calculateAllOrbits(orbitMap: Map<String, List<String>>): Int {
    return orbitMap.keys.map { calculateOrbits(orbitMap, it) }.sum()
}

fun calculateOrbits(orbitMap: Map<String, List<String>>, planet: String): Int {
    val directOrbitedPlanets = orbitMap[planet]

    return (directOrbitedPlanets?.size ?: 0) + (directOrbitedPlanets?.sumBy { calculateOrbits(orbitMap, it) } ?: 0)
}
