package year2021

import Puzzle

fun main() = Puzzle.main(Day25)

object Day25 : Puzzle2021(25) {
    val part1 by solution {
        val grid = input.map { it.toCharArray() }
        var steps = 0
        do { steps++ } while(grid.step('>', dx = 1) + grid.step('v', dy = 1) > 0)
        steps
    }

    private fun List<CharArray>.step(cucumber: Char, dx: Int = 0, dy: Int = 0): Int {
        val width = first().size
        val height = size
        val canMove = first().indices.flatMap { x ->
            indices.filter { y ->
                this[y][x] == cucumber && this[(y + dy) % height][(x + dx) % width] == '.'
            }.map(x::to)
        }
        canMove.forEach { (x, y) ->
            this[y][x] = '.'
            this[(y + dy) % height][(x + dx) % width] = cucumber
        }
        return canMove.size
    }
}
