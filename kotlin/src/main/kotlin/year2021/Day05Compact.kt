package year2021

import Puzzle
import SolutionCtx

object Day05Compact : Puzzle2021(5, {
    infix fun Int.through(other: Int) = if (this > other) downTo(other) else rangeTo(other)
    fun <T> cycle(vararg elements: T) = generateSequence { elements.asSequence() }.flatten().asIterable()
    fun SolutionCtx.countDangers(includeDiagonals: Boolean) = input.asSequence()
            .map { it.split(" -> ", ",").map(String::toInt) }
            .filter { (x1, y1, x2, y2) -> includeDiagonals || x1 == x2 || y1 == y2 }
            .flatMap { (x1, y1, x2, y2) ->
                (if (x1 == x2) cycle(x1) else x1 through x2)
                    .zip(if (y1 == y2) cycle(y1) else y1 through y2)
            }
            .groupBy { it }.count { (_, v) -> v.size > 1 }
    part1 { countDangers(includeDiagonals = false) }
    part2 { countDangers(includeDiagonals = true) }
})

fun main() = Puzzle.main(Day05Compact)
