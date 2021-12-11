package year2021

import Puzzle
import io.kotest.core.spec.style.FunSpec
import io.kotest.inspectors.forAll
import solutionsShouldReturn

class Year2021Test : FunSpec({
    Puzzle.all<Puzzle2021>().forAll {
        test(it::class.qualifiedName!!) {
            when (it.day) {
                1 -> it.solutionsShouldReturn(part1 = 7, part2 = 5)
                2 -> it.solutionsShouldReturn(part1 = 150, part2 = 900)
                3 -> it.solutionsShouldReturn(part1 = 198, part2 = 230)
                4 -> it.solutionsShouldReturn(part1 = 4512, part2 = 1924)
                5 -> it.solutionsShouldReturn(part1 = 5, part2 = 12)
                6 -> it.solutionsShouldReturn(part1 = 5934, part2 = 26984457539)
                7 -> it.solutionsShouldReturn(part1 = 37, part2 = 168)
                8 -> it.solutionsShouldReturn(part1 = 26, part2 = 61229)
                9 -> it.solutionsShouldReturn(part1 = 15, part2 = 1134)
                10 -> it.solutionsShouldReturn(part1 = 26397, part2 = 288957)
                11 -> it.solutionsShouldReturn(part1 = 1656, part2 = Unit)
            }
        }
    }
})
