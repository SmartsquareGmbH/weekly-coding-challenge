@file:JvmName("App")

package com.rubengees.day6

import java.nio.file.Files
import java.nio.file.Paths

fun main() {
    val input = Files.readAllLines(Paths.get(object {}.javaClass.classLoader.getResource("input.txt")!!.toURI()))
    val orbitMap = parse(input)

    println(calculateAllOrbits(orbitMap))
    println(calculateShortestWay(orbitMap, "YOU", "SAN"))
}

fun parse(input: List<String>): Map<String, String> = input
    .map { it.split(")") }
    .associate { (to, from) -> from to to }

fun calculateAllOrbits(orbitMap: Map<String, String>): Int = orbitMap.keys.map { calculateOrbits(orbitMap, it) }.sum()

fun calculateOrbits(orbitMap: Map<String, String>, planet: String): Int = iterateOrbits(orbitMap, planet).size

fun calculateShortestWay(orbitMap: Map<String, String>, start: String, end: String): Int {
    val intersection = requireNotNull(findIntersection(orbitMap, start, end)) {
        "No intersection between $start and $end found"
    }

    val startToIntersection = iterateOrbits(orbitMap, start, intersection)
    val endToIntersection = iterateOrbits(orbitMap, end, intersection)

    return startToIntersection.size - 1 + endToIntersection.size - 1
}

fun findIntersection(orbitMap: Map<String, String>, planet1: String, planet2: String): String? {
    return iterateOrbits(orbitMap, planet1).intersect(iterateOrbits(orbitMap, planet2)).firstOrNull()
}

private fun iterateOrbits(orbitMap: Map<String, String>, start: String, end: String? = null): List<String> {
    return when (val orbitedPlanet = orbitMap[start]) {
        null -> emptyList()
        end -> listOf(orbitedPlanet)
        else -> listOf(orbitedPlanet) + iterateOrbits(orbitMap, orbitedPlanet, end)
    }
}
