package year2021

import Puzzle
import SolutionCtx

private typealias State = List<Long>

object Day06 : Puzzle2021(6, {
    fun SolutionCtx.initialState(): State = input.first().split(',')
        .groupingBy(String::toInt).eachCount()
        .run { List(9) { getOrDefault(it, 0).toLong() } }

    fun State.simulate(days: Int): State = (1 .. days).fold(this) { state, _ ->
        List(9) { state[(it + 1) % 9] + if (it == 6) state[0] else 0 }
    }

    part1 {
        initialState().simulate(80).sum()
    }

    part2 {
        initialState().simulate(256).sum()
    }
})

fun main() = Puzzle.main(Day06)
