@file:JvmName("Utils")

package com.rubengees.day9

import kotlin.math.pow

fun Long.digits(): Sequence<Long> {
    var current = this

    return sequence {
        while (current != 0L) {
            yield(current % 10)

            current /= 10
        }
    }
}

fun Iterable<Long>.mergeDigits() = this
    .mapIndexed { index, i ->
        i * (10.0.pow((index).toDouble())).toInt()
    }
    .sum()

fun Sequence<Long>.mergeDigits() = this.toList().mergeDigits()
