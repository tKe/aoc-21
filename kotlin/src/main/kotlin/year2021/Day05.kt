package year2021

import Puzzle
import SolutionCtx

object Day05 : Puzzle2021(5, {
    infix fun Int.through(other: Int) = if (this > other) downTo(other) else rangeTo(other)
    data class Coord(val x: Int, val y: Int)
    data class Line(val from: Coord, val to: Coord) {
        val isHorizontal = from.y == to.y
        val isVertical = from.x == to.x
        val isDiagonal = !isVertical && !isHorizontal
        val coords by lazy {
            when {
                from.x == to.x -> (from.y through to.y).map { from.x to it }
                from.y == to.y -> (from.x through to.x).map { it to from.y }
                else -> (from.x through (to.x)) zip (from.y through to.y)
            }
        }
    }

    fun SolutionCtx.parseLines() = input.map {
        it.split(" -> ", limit = 2)
            .map { c ->
                c.split(",", limit = 2)
                    .map(String::toInt)
                    .let { (x, y) -> Coord(x, y) }
            }
            .let { (from, to) -> Line(from, to) }
    }

    part1 {
        parseLines()
            .filterNot(Line::isDiagonal)
            .flatMap(Line::coords)
            .groupingBy { it }
            .eachCount()
            .count { (_, v) -> v >= 2 }
    }

    part2 {
        parseLines()
            .flatMap(Line::coords)
            .groupingBy { it }
            .eachCount()
            .count { (_, v) -> v >= 2 }
    }
})

fun main() = Puzzle.main(Day05)
