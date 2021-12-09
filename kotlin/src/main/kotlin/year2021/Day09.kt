package year2021

import Puzzle

object Day09 : Puzzle2021(9, {
    data class Grid(val data: List<List<Int>>) {
        operator fun get(point: Pair<Int, Int>) = point.let { (x, y) -> data[y][x] }
        val rows = data.indices
        val cols = data.first().indices
        val indices = cols.asSequence().flatMap { rows.map(it::to) }
        fun neighbours(point: Pair<Int, Int>) =
            point.let { (x, y) -> sequenceOf((x - 1) to y, (x + 1) to y, x to (y - 1), x to (y + 1)) }
                .filter { (x, y) -> x in cols && y in rows }
    }

    fun <R> List<String>.parseGrid(block: Grid.() -> R) = Grid(map { it.map(Char::digitToInt) }).block()

    part1 {
        input.parseGrid {
            indices.sumOf {
                get(it).let { value ->
                    if (neighbours(it).map(::get).all { n -> n > value }) value + 1 else 0
                }
            }
        }
    }

    part2 {
        input.parseGrid {
            indices.filter { this[it] < neighbours(it).minOf(::get) }
                .map { min ->
                    generateSequence(setOf(min) to listOf(min)) { (basin, candidates) ->
                        candidates.flatMap(::neighbours).distinct()
                            .filterNot(basin::contains).filter { this[it] < 9 }
                            .let { (basin + it) to it }
                    }.firstNotNullOf { (basin, candidates) -> basin.takeIf { candidates.isEmpty() } }.size
                }
                .sortedDescending().take(3)
                .fold(1, Int::times)
        }
    }
})

fun main() = Puzzle.main(Day09)
