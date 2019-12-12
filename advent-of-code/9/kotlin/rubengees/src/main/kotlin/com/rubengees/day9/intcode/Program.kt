package com.rubengees.day9.intcode

class Program private constructor(private val memory: List<Int>, private val inputs: List<Int> = emptyList()) {

    companion object {
        fun parse(rawProgram: String): Program {
            return rawProgram.split(",")
                .map { it.trim().toInt() }
                .let { Program(it) }
        }
    }

    val length = memory.size
    val input = inputs.firstOrNull()

    operator fun get(index: Int) = memory[index]

    fun withInput(value: Int) = Program(memory, inputs + value)
    fun withInputs(vararg values: Int) = Program(memory, inputs + values.toList())

    fun update(index: Int, value: Int) = memory.toMutableList()
        .apply { set(index, value) }
        .let { Program(it, inputs) }

    fun shiftInputs() = Program(memory, inputs.drop(1))

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Program

        if (memory != other.memory) return false

        return true
    }

    override fun hashCode(): Int {
        return memory.hashCode()
    }

    override fun toString() = "$memory"
}
