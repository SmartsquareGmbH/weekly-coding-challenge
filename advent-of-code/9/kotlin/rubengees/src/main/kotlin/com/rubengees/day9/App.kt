@file:JvmName("App")

package com.rubengees.day9

import com.rubengees.day9.intcode.IntCodeExecutor
import com.rubengees.day9.intcode.Program
import java.nio.file.Files
import java.nio.file.Paths

fun main() {
    val input = Files.readString(Paths.get(object {}.javaClass.classLoader.getResource("input.txt")!!.toURI()))
    val program = Program.parse(input)

    println(IntCodeExecutor.run(program.withInput(1)).outputs)
    println(IntCodeExecutor.run(program.withInput(2)).outputs)
}
