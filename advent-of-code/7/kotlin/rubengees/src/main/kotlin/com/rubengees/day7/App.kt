@file:JvmName("App")

package com.rubengees.day7

import com.rubengees.day7.intcode.IntCodeExecutor
import com.rubengees.day7.intcode.Program
import java.nio.file.Files
import java.nio.file.Paths

fun main() {
    val input = Files.readString(Paths.get(object {}.javaClass.classLoader.getResource("input.txt")!!.toURI()))
    val program = Program.parse(input)

    val result = IntCodeExecutor.run(program)

    println(result.outputs)
}
