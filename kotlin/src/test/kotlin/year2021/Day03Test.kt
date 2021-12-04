package year2021

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import testContext

class Day03Test : FunSpec({
    with(Day03.testContext()) {
        context("part1") {
            test("example input produces result of 198") {
                puzzle["part1"]() shouldBe 198
            }
        }

        context("part2") {
            test("example input produces result of 230") {
                puzzle["part2"]() shouldBe 230
            }
        }
    }
})
