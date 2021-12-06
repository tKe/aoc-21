package year2021

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import testContext

class Day06Test : FunSpec({
    with(Day06.testContext()) {
        context("part1") {
            test("example input produces result of 5934") {
                puzzle["part1"]() shouldBe 5934
            }
        }

        context("part2") {
            test("example input produces result of <unkn>>") {
                puzzle["part2"]() shouldBe Unit
            }
        }
    }
})
