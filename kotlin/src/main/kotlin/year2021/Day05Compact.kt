package year2021

import Puzzle
import SolutionCtx

object Day05Compact : Puzzle2021(5, {
    infix fun Int.through(other: Int) = if (this > other) downTo(other) else rangeTo(other)
    fun <T> cycle(vararg elements: T) = generateSequence { elements.asSequence() }.flatten().asIterable()
    fun SolutionCtx.countDangers(filter: (List<Int>) -> Boolean = { true }) = input.asSequence()
        .map { it.split(" -> ", ",").map(String::toInt) }.filter(filter)
        .flatMap { (a, b, c, d) -> (if (a == c) cycle(a) else a through c) zip (if (b == d) cycle(b) else b through d) }
        .groupBy { it }.count { (_, v) -> v.size > 1 }
    part1 { countDangers { (a, b, c, d) -> a == c || b == d } }
    part2 { countDangers() }
})

fun main() = Puzzle.main(Day05Compact)
