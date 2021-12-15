package year2021

import Puzzle
import java.util.PriorityQueue

fun main() = Puzzle.main(Day15)

object Day15 : Puzzle2021(15, {

    data class Point(val x: Int, val y: Int)
    class Grid(val data: IntArray, val stride: Int) {
        constructor(cols: Int, rows: Int, initialise: (x: Int, y: Int) -> Int)
                : this(IntArray(cols * rows) { initialise(it % cols, it / cols) }, cols)

        init {
            check(data.size % stride == 0)
        }

        val cols = stride
        val rows = data.size / stride

        operator fun contains(point: Point) = with(point) { x in colRange && y in rowRange }
        operator fun get(point: Point) = data[point.index]
        operator fun get(x: Int, y: Int) = data[y * stride + x]
        operator fun set(point: Point, value: Int) {
            data[point.index] = value
        }

        fun neighbours(point: Point) = with(point) {
            sequenceOf(
                Point(x - 1, y),
                Point(x + 1, y),
                Point(x, y - 1),
                Point(x, y + 1)
            ).filter(::contains)
        }

        private val rowRange = 0 until rows
        private val colRange = 0 until cols
        private val Point.index get() = y * stride + x
    }

    fun Grid.dijkstra(start: Point = Point(0, 0), end: Point = Point(cols - 1, rows - 1)): Int {
        val dist = Grid(cols, rows) { x, y -> if (start.x == x && start.y == y) 0 else Int.MAX_VALUE }
        val queue = PriorityQueue(Comparator.comparing(dist::get)).also { it += start }

        while(queue.isNotEmpty()) {
            val current = queue.poll()
            if (current == end) return dist[end]
            for (neighbour in neighbours(current)) {
                val alt = dist[current] + this[neighbour]
                if (alt < dist[neighbour]) {
                    dist[neighbour] = alt
                    queue += neighbour
                }
            }
        }

        return -1
    }

    fun Grid.scale(scale: Int) = Grid(cols * scale, rows * scale) { x, y ->
        (this[x % cols, y % rows] + (x / cols) + (y / rows)).let { 1 + ((it - 1) % 9) }
    }

    fun List<String>.parseGrid() =  Grid(flatMap { it.map(Char::digitToInt) }.toIntArray(), first().length)

    part1 {
        input.parseGrid().dijkstra()
    }

    part2 {
        input.parseGrid().scale(5).dijkstra()
    }
})
