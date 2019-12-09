package com.rubengees.day5

import com.rubengees.day5.Opcode.Argument.ArgumentMode

object ArgumentParser {

    fun parse(program: Program, count: Int, instructionPointer: Int): List<Opcode.Argument> {
        val argumentModes = program[instructionPointer].digits().drop(2).toList()

        return (0 until count)
            .map { argumentModes.getOrElse(it) { 0 } }
            .map { it.toArgumentMode() }
            .mapIndexed { index, mode -> Opcode.Argument(program[instructionPointer + 1 + index], mode) }
    }

    private fun Int.toArgumentMode() = when (this) {
        0 -> ArgumentMode.POSITION
        1 -> ArgumentMode.IMMEDIATE
        else -> error("Invalid argument mode: $this")
    }
}
