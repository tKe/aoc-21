package year2021

import Puzzle
import SolutionCtx

object Day04 : Puzzle2021(4, {
    solution("part1") {
        val (calls, grids) = game
        calls.forEach { call ->
            grids.forEach { grid ->
                grid.play(call)
                if (grid.bingo) {
                    return@solution call * grid.score
                }
            }
        }
        error("No solution")
    }

    solution("part2") {
        val (calls, grids) = game
        calls.forEach { call ->
            grids.forEach { grid ->
                grid.play(call)
                if (grids.all { it.bingo }) {
                    return@solution call * grid.score
                }
            }
        }
        error("No solution")
    }
})

@JvmInline
private value class Grid(private val arr: IntArray = IntArray(26)) {
    val bingo: Boolean get() = checkMarks(5, rowMask) || checkMarks(1, colMask)
    val score: Int
        get() = arr.foldIndexed(0) { index, acc, value ->
            acc + if (isMarked(index) || index == 25) 0 else value
        }

    operator fun set(row: Int, col: Int, value: Int) = arr.set(idx(row, col), value)
    operator fun get(row: Int, col: Int) = arr[idx(row, col)]

    fun play(value: Int) = repeat(25) { idx -> if (arr[idx] == value) mark(idx) }

    private fun checkMarks(shift: Int, mask: Int) =
        generateSequence(marks) { it shr shift }.take(5).any { (it and mask) == mask }

    private fun idx(row: Int, col: Int): Int {
        check(row < 5 && col < 5)
        return row * 5 + col
    }

    private fun mark(index: Int) {
        marks = marks or (1 shl index)
    }

    private fun isMarked(index: Int) = (marks shr index) and 1 == 1

    private var marks
        get() = arr[25]
        set(value) {
            arr[25] = value
        }

    override fun toString() =
        (0 until 5).joinToString("\n") { row ->
            (0 until 5).joinToString(" ") { col ->
                val s = get(row, col).toString().padStart(2, ' ')
                if (isMarked(idx(row, col))) "($s)"
                else " $s "
            }
        }

    companion object {
        private const val rowMask = 0b11111
        private const val colMask = 0b100001000010000100001
    }
}

private val SolutionCtx.game
    get() = withInput { lines ->
        with(lines.iterator()) {
            check(hasNext()) { "empty input" }
            val calls = next().splitToSequence(',').map(String::toInt).toList()
            val grids = buildList {
                while (hasNext()) {
                    check(next().isBlank()) { "unexpected content" }
                    this += Grid().also { grid ->
                        repeat(5) { row ->
                            check(hasNext()) { "not enough rows for grid" }
                            next().splitToSequence(" ")
                                .filter { it.isNotBlank() }
                                .map(String::toInt)
                                .forEachIndexed { col, value -> grid[row, col] = value }
                        }
                    }
                }
            }

            calls to grids
        }
    }


fun main() = Puzzle.main(Day04)
