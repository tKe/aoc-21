package year2021

import Puzzle
import SolutionCtx
import kotlin.math.abs

object Day07 : Puzzle2021(7, {
    fun SolutionCtx.crabPositions() = input.single().split(",").map(String::toInt).sorted()
    fun List<Int>.fuel(pos: Int, usageRate: (Int) -> Int = { it }) = sumOf { usageRate(abs(pos - it)) }

    part1 {
        with(crabPositions()) { fuel(this[size / 2]) }
    }

    part2 {
        with(crabPositions()) {
            average().toInt().let { avg -> (-1..1).minOf { fuel(avg + it) { n -> n * (n + 1) / 2} } }
        }
    }
})

fun main() = Puzzle.main(Day07)
