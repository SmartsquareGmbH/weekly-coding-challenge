@file:JvmName("App")

package com.rubengees.day5

import java.nio.file.Files
import java.nio.file.Paths

fun main() {
    val input = Files.readString(Paths.get(object {}.javaClass.classLoader.getResource("input.txt")!!.toURI()))
    val program = Program.parse(input)

    println(program)
}
