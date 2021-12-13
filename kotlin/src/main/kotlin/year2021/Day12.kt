package year2021

import Puzzle
import SolutionCtx

fun main() = Puzzle.main(Day12)

object Day12 : Puzzle2021(12, {

    fun SolutionCtx.parseCaves() = buildMap<String, List<String>> {
        input.forEach {
            it.split("-").let { (a, b) ->
                if (b != "start") compute(a) { _, it -> it.orEmpty() + b }
                if (a != "start") compute(b) { _, it -> it.orEmpty() + a }
            }
        }
    }

    fun Map<String, List<String>>.traverse(
        history: List<String> = listOf("start"),
        allowSecondVisit: Boolean = false
    ): Sequence<List<String>> =
        sequence {
            val currentCave = history.last()
            val smallCavesVisited = history.filter { it[0].isLowerCase() }.toSet()
            for (it in getValue(currentCave)) {
                when (it) {
                    "end" -> yield(history + it)
                    in smallCavesVisited -> if (allowSecondVisit) yieldAll(traverse(history + it, false))
                    else -> yieldAll(traverse(history + it, allowSecondVisit))
                }
            }
        }


    part1 {
        parseCaves().traverse().count()
    }
    part2 {
        parseCaves().traverse(allowSecondVisit = true).count()
    }
})
