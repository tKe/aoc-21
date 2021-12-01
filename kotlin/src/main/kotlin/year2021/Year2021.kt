package year2021

import Puzzle

sealed class Puzzle2021(day: Int, def: PuzzleCtx.() -> Unit) : Puzzle(2021, day, def)

fun main() = Puzzle.main<Puzzle2021>()
