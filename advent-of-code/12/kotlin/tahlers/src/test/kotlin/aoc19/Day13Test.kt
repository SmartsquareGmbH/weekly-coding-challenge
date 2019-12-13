package aoc19

import aoc19.Day13.TileType.BLOCK
import io.kotlintest.shouldBe
import io.kotlintest.specs.FreeSpec
import io.vavr.collection.HashMap

class Day13Test : FreeSpec({

    val program = Day13.readProgram(this.javaClass.getResource("/day13_1.txt").readText())

    "calculate solution one" - {

        val screen = Day13.computeScreen(program)
        val result = screen.filter { it.type == BLOCK }
        result.size() shouldBe 315

    }


})