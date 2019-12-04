package com.github.deen13.one

class Spacecraft(modules: List<Module>) {

    val fuel = modules.map { it.mass }.sumBy { recursiveCalculateFuel(it) }

    private fun calculateFuel(mass: Int) = mass / 3 - 2

    private fun recursiveCalculateFuel(mass: Int): Int {
        val fuel = calculateFuel(mass)

        return if (mass < 9) 0 else fuel + recursiveCalculateFuel(fuel)
    }
}
