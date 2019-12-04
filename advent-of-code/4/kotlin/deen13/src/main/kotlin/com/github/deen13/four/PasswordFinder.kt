package com.github.deen13.four

import kotlin.streams.toList

fun Int.hasNoDecreasingPairOfDigits(): Boolean {
    val sorted = this.toString().chars().sorted().toList()
    val unsorted = this.toString().chars().toList()

    return sorted == unsorted
}

fun Int.isSixDigitsLong() = this.toString().length == 6

fun Int.hasAdjacentDigits(): Boolean {
    val chars = this.toString().chars().toList()

    return chars.distinct().count() < chars.count()
}

fun Int.hasOnePairOfAdjacentDigits(): Boolean {
    val chars = this.toString().chars().toList()

    return ('0'.toInt()..'9'.toInt()).any { chars.lastIndexOf(it) - chars.indexOf(it) == 1 }
}

fun findPasswordsInRange(
    range: IntRange,
    filters: List<(Int) -> Boolean>
): List<Int> = range.filter { p -> filters.all { func -> func(p) } }
