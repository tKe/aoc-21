package year2021

import Puzzle
import io.kotest.assertions.fail
import io.kotest.core.spec.style.FunSpec
import io.kotest.core.test.AssertionMode
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.inspectors.forAll
import io.kotest.matchers.shouldBe
import solutionsShouldReturn

class Year2021Test : FunSpec({
    assertions = AssertionMode.Error
    assertSoftly = true
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
                11 -> it.solutionsShouldReturn(part1 = 1656, part2 = 195)
                12 -> {
                    it.solutionsShouldReturn(part1 = 10, part2 = 36, inputFile = "example-simple.txt")
                    it.solutionsShouldReturn(part1 = 19, part2 = 103, inputFile = "example-larger.txt")
                    it.solutionsShouldReturn(part1 = 226, part2 = 3509)
                }
                13 -> it.solutionsShouldReturn(
                    part1 = 17,
                    part2 = """
                        █████
                        █░░░█
                        █░░░█
                        █░░░█
                        █████
                        ░░░░░
                        ░░░░░
                    """.trimIndent())
                14 -> it.solutionsShouldReturn(part1 = 1588, part2 = 2188189693529)
                15 -> it.solutionsShouldReturn(part1 = 40, part2 = 315)
                16 -> {
                    it.solutionsShouldReturn(part1 = listOf(16, 12, 23, 31), inputFile = "example-part1.txt")
                    it.solutionsShouldReturn(
                        part2 = listOf(3, 54, 7, 9, 1, 0, 0, 1),
                        inputFile = "example-part2.txt"
                    )
                }
                17 -> it.solutionsShouldReturn(part1 = 45, part2 = 112)
                18 -> it.solutionsShouldReturn(part1 = 4140, part2 = 3993)
                19 -> it.solutionsShouldReturn(part1 = 79, part2 = 3621)
                20 -> it.solutionsShouldReturn(part1 = 35, 3351)
                21 -> it.solutionsShouldReturn(part1 = 739785, part2 = 444356092776315L)
                22 -> {
                    it.solutionsShouldReturn(part1 = 590784, inputFile = "example-part1.txt")
                    it.solutionsShouldReturn(part2 = 2758514936282235, inputFile = "example-part2.txt")
                }
                else -> fail("no tests for day ${it.day}")
            }
        }
    }

    context("additional Day18 test cases") {
        forAll(
            row(
                """
                    [1,1]
                    [2,2]
                    [3,3]
                    [4,4]
                """.trimIndent(), "[[[[1,1],[2,2]],[3,3]],[4,4]]"
            ),
            row(
                """
                    [1,1]
                    [2,2]
                    [3,3]
                    [4,4]
                    [5,5]
                """.trimIndent(), "[[[[3,0],[5,3]],[4,4]],[5,5]]"
            ),
            row(
                """
                    [1,1]
                    [2,2]
                    [3,3]
                    [4,4]
                    [5,5]
                    [6,6]
                """.trimIndent(), "[[[[5,0],[7,4]],[5,5]],[6,6]]"
            ),
            row(
                """
                    [[[0,[4,5]],[0,0]],[[[4,5],[2,6]],[9,5]]]
                    [7,[[[3,7],[4,3]],[[6,3],[8,8]]]]
                    [[2,[[0,8],[3,4]]],[[[6,7],1],[7,[1,6]]]]
                    [[[[2,4],7],[6,[0,5]]],[[[6,8],[2,8]],[[2,1],[4,5]]]]
                    [7,[5,[[3,8],[1,4]]]]
                    [[2,[2,2]],[8,[8,1]]]
                    [2,9]
                    [1,[[[9,3],9],[[9,0],[0,7]]]]
                    [[[5,[7,4]],7],1]
                    [[[[4,2],2],6],[8,7]]
                """.trimIndent(), "[[[[8,7],[7,7]],[[8,6],[7,7]]],[[[0,7],[6,6]],[8,7]]]"
            )
        ) { input, result ->
            Day18.sumAll(input.lines()).toString() shouldBe result
        }
    }
})
