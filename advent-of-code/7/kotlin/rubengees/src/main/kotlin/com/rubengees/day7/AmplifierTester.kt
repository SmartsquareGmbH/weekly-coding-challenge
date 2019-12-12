package com.rubengees.day7

import com.rubengees.day7.intcode.IntCodeExecutor
import com.rubengees.day7.intcode.IntCodeExecutor.ProgramExecutionResult
import com.rubengees.day7.intcode.Program

object AmplifierTester {

    fun run(program: Program): Int? {
        val sequences = generateThrusterSequences((0..4).toList())

        val outputs = sequences.map { inputs ->
            inputs.fold(0) { acc, nextInput ->
                IntCodeExecutor.run(program.withInputs(nextInput, acc)).outputs.first()
            }
        }

        return outputs.max()
    }

    fun runFeedbackLoop(program: Program): Int? {
        val sequences = generateThrusterSequences((5..9).toList())

        // TODO: Find a way to make this more functional.
        val outputs = sequences.map { inputs ->
            var states = inputs.map { ProgramExecutionResult(program) }

            // Iterate over the inputs until all programs are halted.
            inputs.asSequence().repeat().takeWhile { !states.all { state -> state.isHalted } }.forEach { input ->
                val (_, _, previousOutputs, _) = states.last()
                val (currentProgram, currentInstructionPointer, currentOutputs, _) = states.first()

                // Only pass in the input from the sequence the first time (which is the case when the program has
                // no outputs yet).
                val nextInputs = if (currentOutputs.isEmpty()) {
                    listOf(input, previousOutputs.lastOrNull() ?: 0)
                } else {
                    listOf(previousOutputs.lastOrNull() ?: 0)
                }

                val nextProgram = currentProgram.withInputs(*nextInputs.toIntArray())
                val nextState = IntCodeExecutor.run(nextProgram, currentInstructionPointer, true)

                // If the state is halted, take the outputs from the current execution,
                // because we don't get outputs on final halt.
                states = when (nextState.isHalted) {
                    true -> states.drop(1) + nextState.copy(outputs = currentOutputs)
                    false -> states.drop(1) + nextState
                }
            }

            states.last().outputs.last()
        }

        return outputs.max()
    }

    private fun generateThrusterSequences(remainingRange: List<Int>): List<List<Int>> {
        return remainingRange.flatMap { value ->
            val recursiveSequences = generateThrusterSequences(remainingRange - value)

            if (recursiveSequences.isEmpty()) {
                listOf(listOf(value))
            } else {
                recursiveSequences.map { recursiveValues -> listOf(value) + recursiveValues }
            }
        }
    }

    private fun <T> Sequence<T>.repeat() = sequence { while (true) yieldAll(this@repeat) }
}
