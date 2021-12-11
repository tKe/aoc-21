package year2021

import Puzzle

fun main() = Puzzle.main(Day11)

object Day11 : Puzzle2021(11, {
    data class Grid(val data: List<List<Int>>) {
        operator fun get(point: Pair<Int, Int>) = point.let { (x, y) -> if (x in cols && y in rows) data[y][x] else 10 }
        val rows = data.indices
        val cols = data.first().indices
        val indices = cols.asSequence().flatMap { rows.map(it::to) }
        fun neighbours(point: Pair<Int, Int>) =
            point.let { (x, y) ->
                sequenceOf(
                    (x - 1) to (y - 1), x to (y - 1), (x + 1) to (y - 1),
                    (x - 1) to y,/*                */ (x + 1) to y,
                    (x - 1) to (y + 1), x to (y + 1), (x + 1) to (y + 1)
                )
            }
    }

    fun Grid.mapValues(transform: (point: Pair<Int, Int>, value: Int) -> Int) =
        Grid(rows.map { y -> cols.map { x -> (x to y).let { transform(it, this[it]) } } })

    fun Grid.flashingIndices() = indices.filter { this[it] > 9 }.toSet()

    fun Grid.tick(): Grid {
        val incremented = mapValues { _, value -> value + 1 }
        val cascade = generateSequence(incremented to incremented.flashingIndices()) { (grid, flashed) ->
            val increments = flashed.asSequence()
                .flatMap(grid::neighbours)
                .filterNot { grid[it] > 9 }
                .groupingBy { it }.eachCount()

            if (increments.isEmpty()) return@generateSequence grid to emptySet<Pair<Int, Int>>()

            val cascaded = grid.mapValues { point, value -> value + (increments[point] ?: 0) }
            cascaded to cascaded.flashingIndices() - grid.flashingIndices()
        }.firstNotNullOf { (grid, flashed) -> grid.takeIf { flashed.isEmpty() } }
        return cascade.mapValues { _, value -> if (value > 9) 0 else value }
    }

    fun <R> List<String>.parseGrid(block: Grid.() -> R) = Grid(map { it.map(Char::digitToInt) }).block()

    part1 {
        input.parseGrid {
            generateSequence(tick(), Grid::tick).take(100)
                .sumOf { grid -> grid.indices.count { grid[it] == 0 } }
        }
    }

    part2 {
        input.parseGrid {
            generateSequence(this, Grid::tick).map { grid ->
                grid.indices.count { grid[it] == 0 }
            }.takeWhile { it < 100 }.count()
        }
    }
})
