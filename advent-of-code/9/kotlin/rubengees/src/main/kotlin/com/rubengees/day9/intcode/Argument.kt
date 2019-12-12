package com.rubengees.day9.intcode

import com.rubengees.day9.digits

data class Argument(val value: Long, val mode: ArgumentMode) {

    companion object {
        fun parse(program: Program, count: Int, instructionPointer: Long): List<Argument> {
            val argumentModes = program[instructionPointer].digits().drop(2).toList()

            return (0 until count)
                .map { argumentModes.getOrElse(it) { 0 } }
                .map { it.toArgumentMode() }
                .mapIndexed { index, mode -> Argument(program[instructionPointer + 1 + index], mode) }
        }

        private fun Long.toArgumentMode() = when (this) {
            0L -> ArgumentMode.POSITION
            1L -> ArgumentMode.IMMEDIATE
            2L -> ArgumentMode.RELATIVE
            else -> error("Invalid argument mode: $this")
        }
    }

    fun resolve(program: Program) = when (mode) {
        ArgumentMode.POSITION -> program[value]
        ArgumentMode.IMMEDIATE -> value
        ArgumentMode.RELATIVE -> program[program.relativeBase + value]
    }

    fun resolveOutput(program: Program) = when (mode) {
        ArgumentMode.RELATIVE -> program.relativeBase + value
        else -> value
    }

    enum class ArgumentMode { POSITION, IMMEDIATE, RELATIVE }
}
