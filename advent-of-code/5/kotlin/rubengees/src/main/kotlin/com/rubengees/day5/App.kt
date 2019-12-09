@file:JvmName("App")

package com.rubengees.day5

import com.rubengees.day5.intcode.IntCodeExecutor
import com.rubengees.day5.intcode.Program
import java.nio.file.Files
import java.nio.file.Paths

fun main() {
    val day2 = Files.readString(Paths.get(object {}.javaClass.classLoader.getResource("day2.txt")!!.toURI()))
    val day5 = Files.readString(Paths.get(object {}.javaClass.classLoader.getResource("day5.txt")!!.toURI()))

    val day2Program = Program.parse(day2)
    val day5Program = Program.parse(day5)

    day2Part1(day2Program)
    day2Part2(day2Program)
    day5Part1(day5Program)
}

private fun day2Part1(program: Program) {
    val updatedProgram = program.update(1, 12).update(2, 2)
    val result = IntCodeExecutor.run(updatedProgram)

    println(result.program[0])
}

private fun day2Part2(program: Program) {
    val result = (0..99)
        .flatMap { noun -> (0..99).map { verb -> noun to verb } }.asSequence()
        .map { (noun, verb) -> Triple(noun, verb, program.update(1, noun).update(2, verb)) }
        .map { (noun, verb, currentProgram) -> Triple(noun, verb, IntCodeExecutor.run(currentProgram)) }
        .find { (_, _, executionResult) -> executionResult.program[0] == 19690720 }

    requireNotNull(result) { "No combination of verb and noun found to produce the output 19690720" }

    println(100 * result.first + result.second)
}

private fun day5Part1(program: Program) {
    val result = IntCodeExecutor.run(program.withInput(1))

    println(result.outputs)
}
