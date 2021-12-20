package year2021

import Puzzle

sealed class Puzzle2021(day: Int, def: PuzzleCtx.() -> Unit = {}) : Puzzle(2021, day, def) {
    companion object : Sequence<Puzzle2021> {
        private val impls by lazy { Puzzle2021::class.sealedSubclasses.mapNotNull { it.objectInstance } }
        override fun iterator() = impls.iterator()
    }
}

fun main() = Puzzle.main<Puzzle2021>()
