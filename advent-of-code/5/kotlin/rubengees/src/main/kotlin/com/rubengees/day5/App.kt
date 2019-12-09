@file:JvmName("App")

package com.rubengees.day5

import java.nio.file.Files
import java.nio.file.Paths

fun main() {
    val input = Files.readString(Paths.get(object {}.javaClass.classLoader.getResource("input.txt")!!.toURI()))
    val program = Program.parse(input)

    day2Part1(program)
    day2Part2(program)
}

private fun day2Part1(program: Program) {
    val updatedProgram = program.update(1, 12).update(2, 2)
    val finishedProgram = IntCodeExecutor.run(updatedProgram)

    println(finishedProgram[0])
}

private fun day2Part2(program: Program) {
    val result = (0..99)
        .flatMap { noun -> (0..99).map { verb -> noun to verb } }.asSequence()
        .map { (noun, verb) -> Triple(noun, verb, program.update(1, noun).update(2, verb)) }
        .map { (noun, verb, currentProgram) -> Triple(noun, verb, IntCodeExecutor.run(currentProgram)) }
        .find { (_, _, resultProgram) -> resultProgram[0] == 19690720 }

    requireNotNull(result) { "No combination of verb and noun found to produce the output 19690720" }

    println(100 * result.first + result.second)
}
