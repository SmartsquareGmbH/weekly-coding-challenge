package com.rubengees.day7

import com.rubengees.day7.intcode.IntCodeExecutor
import com.rubengees.day7.intcode.Program

object AmplifierTester {

    fun test(program: Program): Int? {
        val sequences = generateSequences((0..4).toList())

        val outputs = sequences.map { inputs ->
            inputs.fold(0) { acc, nextInput ->
                IntCodeExecutor.run(program.withInputs(nextInput, acc)).outputs.first()
            }
        }

        return outputs.max()
    }

    private fun generateSequences(remainingRange: List<Int>): List<List<Int>> {
        return remainingRange.flatMap { value ->
            val recursiveSequences = generateSequences(remainingRange - value)

            if (recursiveSequences.isEmpty()) {
                listOf(listOf(value))
            } else {
                recursiveSequences.map { recursiveValues -> listOf(value) + recursiveValues }
            }
        }
    }
}
