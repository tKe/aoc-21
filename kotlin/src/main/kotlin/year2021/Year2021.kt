package year2021

import Puzzle

sealed class Puzzle2021(day: Int, def: PuzzleCtx.() -> Unit, name: String? = null) : Puzzle(2021, day, name, def)

fun main() = Puzzle.main<Puzzle2021>()
