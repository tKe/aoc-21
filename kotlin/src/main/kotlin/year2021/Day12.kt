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
        filterCreator: (List<String>) -> ((String) -> Boolean)
    ): Sequence<List<String>> =
        sequence {
            val currentCave = history.last()
            val filter = filterCreator(history)
            check(currentCave != "end")
            getValue(currentCave).filter(filter).forEach {
                when (it) {
                    "end" -> yield(history + it)
                    else -> yieldAll(traverse(history + it, filterCreator))
                }
            }
        }

    part1 {
        parseCaves()
            .traverse { history ->
                val smallCavesVisited = history.filter { it[0].isLowerCase() }.toSet()
                ({ it !in smallCavesVisited })
            }
            .count()
    }
    part2 {
        parseCaves()
            .traverse { history ->
                val smallCaveVisits = history.filter { it[0].isLowerCase() }.groupingBy { it }.eachCount()
                val canDoubleVisit = smallCaveVisits.maxOf { it.value } < 2
                ({ canDoubleVisit || it !in smallCaveVisits })
            }
            .count()
    }
})
