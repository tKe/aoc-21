package year2021

import Puzzle
import SolutionCtx

object Day04 : Puzzle2021(4, {
    solution("part1") {
        val (calls, initialGrids) = game
        calls
            .runningFold(-1 to initialGrids) { (_, grids), call ->
                call to grids.map { grid -> grid.play(call) }
            }
            .first { (_, grids) -> grids.any { grid -> grid.bingo } }
            .let { (call, grids) -> call * grids.single { it.bingo }.score }
    }

    solution("part2") {
        val (calls, initialGrids) = game
        calls
            .runningFold(Triple(-1, initialGrids, emptyList<Grid>())) { (_, grids, _), call ->
                val (bingo, remaining) = grids.map { grid -> grid.play(call) }.partition { it.bingo }
                Triple(call, remaining, bingo)
            }
            .first { (_, remaining, _) -> remaining.isEmpty() }
            .let { (call, _, grids) -> call * grids.last().score }
    }
})

private data class Grid(private val arr: List<Int>, private val marks: Int = 0) {
    init {
        check(arr.size == 25)
    }

    val bingo = checkMarks(5, rowMask) || checkMarks(1, colMask)

    val score = arr.foldIndexed(0) { index, acc, value ->
        acc + if (isMarked(index) || index == 25) 0 else value
    }

    fun play(value: Int): Grid = copy(marks = arr.foldIndexed(marks) { index, acc, i ->
        if (i == value) acc or (1 shl index)
        else acc
    })

    private fun checkMarks(shift: Int, mask: Int) =
        generateSequence(marks) { it shr shift }.take(5).any { (it and mask) == mask }

    private fun isMarked(index: Int) = (marks shr index) and 1 == 1

    override fun toString() =
        (0 until 5).joinToString("\n") { row ->
            (0 until 5).joinToString(" ") { col ->
                val s = arr[idx(row, col)].toString().padStart(2, ' ')
                if (isMarked(idx(row, col))) "($s)"
                else " $s "
            }
        }

    companion object {
        private const val rowMask = 0b11111
        private const val colMask = 0b100001000010000100001

        private fun idx(row: Int, col: Int): Int {
            check(row < 5 && col < 5)
            return row * 5 + col
        }
    }
}

private val SolutionCtx.game
    get() = with(input) {
        val calls = first().split(',').map(String::toInt)
        val grids = subList(1, size).chunked(6) { chunk ->
            check(chunk.size == 6 && chunk.first().isBlank()) { "invalid chunk" }
            chunk.flatMap { line -> line.splitToSequence(" ") }
                .filter(String::isNotBlank)
                .map(String::toInt)
                .let(::Grid)
        }
        calls to grids
    }

fun main() = Puzzle.main(Day04)
