@file:JvmName("Utils")

package com.rubengees.day7

import kotlin.math.pow

fun Int.digits(): Sequence<Int> {
    var current = this

    return sequence {
        while (current != 0) {
            yield(current % 10)

            current /= 10
        }
    }
}

fun Iterable<Int>.mergeDigits() = this
    .mapIndexed { index, i ->
        i * (10.0.pow((index).toDouble())).toInt()
    }
    .sum()

fun Sequence<Int>.mergeDigits() = this.toList().mergeDigits()
