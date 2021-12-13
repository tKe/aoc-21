package year2021

import Puzzle
import SolutionCtx
import kotlin.math.min

fun main() = Puzzle.main(Day13)

object Day13 : Puzzle2021(13, {

    fun List<String>.parseDots() = map { it.split(",").map(String::toInt).let { (a, b) -> a to b } }.toSet()
    fun List<String>.parseFolds() = map { it.split(" ", "=").takeLast(2).let { (a, b) -> a to b.toInt() } }
    fun List<String>.splitAt(needle: String) = indexOf(needle).let { subList(0, it) to subList(it + 1, size) }
    fun SolutionCtx.parseInstructions() = with(input.splitAt("")) { first.parseDots() to second.parseFolds() }

    fun Int?.fold(v: Int) = if (this == null || v <= this) v else this - (v - this)

    fun Set<Pair<Int, Int>>.foldAt(x: Int? = null, y: Int? = null) =
        asSequence().map { (px, py) -> x.fold(px) to y.fold(py) }.toSet()

    fun Set<Pair<Int, Int>>.render(cols: Int = 1 + maxOf { (x, _) -> x }, rows: Int = 1 + maxOf { (_, y) -> y }) =
        (0 until rows).joinToString("\n") { y ->
            (0 until cols).joinToString("") { x -> if (x to y in this@render) "\u2588" else "\u2591" }
        }

    fun List<Pair<String, Int>>.finalDimension() =
        groupingBy { it.first }.fold({ _, v -> v.second }) { _, acc, (_, v) -> min(acc, v) }
            .let { it["x"]!! to it["y"]!! }

    fun Set<Pair<Int, Int>>.fold(folds: Iterable<Pair<String, Int>>) = folds.fold(this) { dots, (axis, n) ->
        when (axis) {
            "x" -> dots.foldAt(x = n)
            "y" -> dots.foldAt(y = n)
            else -> error("can't fold along axis [$axis]")
        }
    }

    part1 {
        val (dots, folds) = parseInstructions()
        dots.fold(folds.take(1)).size
    }
    part2 {
        val (dots, folds) = parseInstructions()
        val (cols, rows) = folds.finalDimension()
        dots.fold(folds).render(cols, rows)
    }
})
