package com.github.deen13.one

import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec

internal class TyrannaOfTheRocketEquation : StringSpec({

    val clazz = TyrannaOfTheRocketEquation::class.java

    "Input" {
        val input = clazz.getResourceAsStream("Input").bufferedReader().readLines()

        val modules = input.map { it.toInt() }.map { Module(it) }

        Spacecraft(modules).fuel shouldBe 4903759
    }
})
