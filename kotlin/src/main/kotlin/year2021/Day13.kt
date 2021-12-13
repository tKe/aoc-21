package year2021

import Puzzle
import SolutionCtx

fun main() = Puzzle.main(Day13)

object Day13 : Puzzle2021(13, {

    fun SolutionCtx.parseInstructions() =
        input.takeWhile { it.isNotBlank() }.map { it.split(",").map(String::toInt).let { (a, b) -> a to b } }.toSet() to
                input.takeLastWhile { it.isNotBlank() }.map { it.split(" ", "=").takeLast(2).let { (a, b) -> a to b.toInt() } }

    fun Int.fold(n: Int) = if (this > n) n - (this - n) else this
    fun Set<Pair<Int, Int>>.foldX(n: Int) = map { (x, y) -> x.fold(n) to y }.toSet()
    fun Set<Pair<Int, Int>>.foldY(n: Int) = map { (x, y) -> x to y.fold(n) }.toSet()
    fun Set<Pair<Int, Int>>.debug() = "Grid Output:\n" + (0..maxOf { it.second}).joinToString("\n") { y->
        (0..maxOf{it.first}).joinToString("") {x ->
                if (x to y in this) "█" else " "
        }
    }
    fun Set<Pair<Int, Int>>.fold(folds: Iterable<Pair<String, Int>>) = folds.fold(this) { dots, (axis, n) ->
        when(axis) {
            "x" -> dots.foldX(n)
            "y" -> dots.foldY(n)
            else -> error("can't fold along axis [$axis]")
        }
    }

    part1 {
        val (dots, folds) = parseInstructions()
        dots.fold(folds.take(1)).size
    }
    part2 {
        val (dots, folds) = parseInstructions()
        dots.fold(folds).debug()
    }
})
