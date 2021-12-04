package year2021

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import testContext

class Day04Test : FunSpec({
    with(Day04.testContext()) {
        context("part1") {
            test("example input produces result of 4512") {
                puzzle["part1"]() shouldBe 4512
            }
        }

        context("part2") {
            test("example input produces result of 1924") {
                puzzle["part2"]() shouldBe 1924
            }
        }
    }
})
