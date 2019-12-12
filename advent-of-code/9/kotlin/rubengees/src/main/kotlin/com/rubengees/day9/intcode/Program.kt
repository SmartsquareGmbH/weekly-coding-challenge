package com.rubengees.day9.intcode

class Program private constructor(
    private val memory: Map<Long, Long>,
    val relativeBase: Long = 0,
    private val inputs: List<Long> = emptyList()
) {

    companion object {
        fun parse(rawProgram: String): Program {
            return rawProgram.split(",")
                .map { it.trim().toLong() }
                .withIndex()
                .associate { (index, value) -> index.toLong() to value }
                .let { Program(it) }
        }
    }

    val length = memory.keys.max()?.plus(1) ?: 0L
    val input = inputs.firstOrNull()

    operator fun get(index: Long) = memory.getOrDefault(index, 0)

    fun withInput(value: Long) = Program(memory, relativeBase, inputs + value)
    fun withInputs(vararg values: Long) = Program(memory, relativeBase, inputs + values.toList())

    fun update(index: Long, value: Long) = memory.toMutableMap()
        .apply { set(index, value) }
        .let { Program(it, relativeBase, inputs) }

    fun adjustRelativeBase(adjustment: Long) = Program(memory, relativeBase + adjustment, inputs)
    fun shiftInputs() = Program(memory, relativeBase, inputs.drop(1))

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

    override fun toString() = "${memory}"
}
