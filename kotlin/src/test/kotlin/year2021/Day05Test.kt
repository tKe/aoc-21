package year2021

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import testContext

class Day05Test : FunSpec({
    with(Day05.testContext()) {
        context("part1") {
            test("example input produces result of 5") {
                puzzle["part1"]() shouldBe 5
            }
        }

        context("part2") {
            test("example input produces result of 12") {
                puzzle["part2"]() shouldBe 12
            }
        }
    }
})
