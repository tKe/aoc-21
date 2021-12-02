package year2021

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import testContext

class Day02Test : FunSpec({
    with(Day02.testContext()) {
        context("part1") {
            test("example input produces result of 150") {
                puzzle["part1"]() shouldBe 150
            }
        }

        context("part2") {
            test("example input produces result of 900") {
                puzzle["part2"]() shouldBe 900
            }
        }
    }
})
