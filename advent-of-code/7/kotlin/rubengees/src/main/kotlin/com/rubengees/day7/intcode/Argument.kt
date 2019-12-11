package com.rubengees.day7.intcode

import com.rubengees.day7.digits

data class Argument(val value: Int, val mode: ArgumentMode) {

    companion object {
        fun parse(program: Program, count: Int, instructionPointer: Int): List<Argument> {
            val argumentModes = program[instructionPointer].digits().drop(2).toList()

            return (0 until count)
                .map { argumentModes.getOrElse(it) { 0 } }
                .map { it.toArgumentMode() }
                .mapIndexed { index, mode -> Argument(program[instructionPointer + 1 + index], mode) }
        }

        private fun Int.toArgumentMode() = when (this) {
            0 -> ArgumentMode.POSITION
            1 -> ArgumentMode.IMMEDIATE
            else -> error("Invalid argument mode: $this")
        }
    }

    fun resolve(program: Program) = when (mode) {
        ArgumentMode.POSITION -> program[value]
        ArgumentMode.IMMEDIATE -> value
    }

    enum class ArgumentMode { POSITION, IMMEDIATE }
}
