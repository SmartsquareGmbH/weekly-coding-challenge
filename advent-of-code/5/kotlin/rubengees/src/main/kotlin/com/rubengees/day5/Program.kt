package com.rubengees.day5

class Program private constructor(private val memory: List<Int>) {

    companion object {
        fun parse(input: String): Program {
            return input.split(",")
                .map { it.trim().toInt() }
                .let { Program(it) }
        }
    }

    val length = memory.size

    operator fun get(index: Int) = memory[index]

    fun update(index: Int, value: Int) = memory.toMutableList()
        .apply { set(index, value) }
        .let { Program(it) }

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
