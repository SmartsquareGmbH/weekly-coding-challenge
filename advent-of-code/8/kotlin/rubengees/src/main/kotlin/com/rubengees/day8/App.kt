@file:JvmName("App")

package com.rubengees.day8

import java.nio.file.Files
import java.nio.file.Paths

fun main() {
    val input = Files.readString(Paths.get(object {}.javaClass.classLoader.getResource("input.txt")!!.toURI()))
    val layers = parse(input.trim(), 25, 6)

    part1(layers)
    part2(layers)
}

private fun part1(layers: List<Layer>) {
    val layerWithLowestAmountOfZeros = layers.minBy { it.numberOfDigits(0) }!!
    val result = layerWithLowestAmountOfZeros.numberOfDigits(1) * layerWithLowestAmountOfZeros.numberOfDigits(2)

    println(result)
}

private fun part2(layers: List<Layer>) {
    val mergedLayers = layers.reduce { acc, layer -> acc.mergeWith(layer) }

    println(mergedLayers)
}

internal fun parse(input: String, width: Int, height: Int): List<Layer> {
    return input
        .asSequence()
        .map { it.toString().toInt() }
        .chunked(width)
        .chunked(height)
        .map { Layer(it) }
        .toList()
}

data class Layer(val pixels: List<List<Int>>) {
    fun numberOfDigits(digit: Int) = pixels.flatten().filter { it == digit }.size

    fun mergeWith(other: Layer): Layer {
        val mergedPixels = this.pixels.zip(other.pixels).map { (pixelsThis, pixelsOther) ->
            pixelsThis.zip(pixelsOther).map { (pixelThis, pixelOther) ->
                if (pixelThis == 2) {
                    pixelOther
                } else {
                    pixelThis
                }
            }
        }

        return Layer(mergedPixels)
    }

    override fun toString(): String {
        return pixels.joinToString("\n") { line ->
            line.joinToString("") { if (it == 0) "█" else " " }
        }
    }
}
