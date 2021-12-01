package year2021

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import testContext

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
