package year2021

import InputCtx
import Puzzle
import SolutionCtx
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class Day01Test : FunSpec({
    with(Day01.testContext()) {
        context("part1") {
            test("example input produces result of 7") {
                Day01["part1"]() shouldBe 7
            }
        }

        context("part2") {
            test("example input produces result of 5") {
                Day01["part2"]() shouldBe 5
            }
        }
    }
})

fun Puzzle.testContext(vararg inputMappings: Pair<String, String> = arrayOf("input.txt" to "example.txt")) =
    SolutionCtx(this, object : InputCtx {
        val remapped = inputMappings.toMap()
        override fun SolutionCtx.resolveInput(name: String): String =
            with(InputCtx.Default) {
                resolveInput(remapped[name] ?: name)
            }
    })

