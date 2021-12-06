package year2021

import Puzzle
import SolutionCtx
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import testContext

class Year2021Test : FunSpec({
    fun Puzzle.solutionsShouldReturn(
        vararg expectedResults: Pair<String, Any>,
        context: Puzzle.() -> SolutionCtx = { testContext() }
    ) = with(context()) {
        context(this@solutionsShouldReturn::class.qualifiedName!!) {
            expectedResults.forEach { (solution, expected) ->
                test("$solution returns $expected") { puzzle[solution]() shouldBe expected }
            }
        }
    }
    fun Puzzle.solutionsShouldReturn(part1: Any = Unit, part2: Any = Unit) =
        solutionsShouldReturn("part1" to part1, "part2" to part2)

    Day01.solutionsShouldReturn(part1 = 7, part2 = 5)
    Day02.solutionsShouldReturn(part1 = 150, part2 = 900)
    Day03.solutionsShouldReturn(part1 = 198, part2 = 230)
    Day04.solutionsShouldReturn(part1 = 4512, part2 = 1924)
    Day05.solutionsShouldReturn(part1 = 5, part2 = 12)
    Day06.solutionsShouldReturn(part1 = 5934, part2 = 26984457539)
})
