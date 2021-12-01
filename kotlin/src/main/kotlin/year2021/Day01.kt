package year2021

import Puzzle

object Day01 : Puzzle2021(1, {
    solution("part1") {
        withInput {
            it.mapNotNull(String::toIntOrNull)
                .zipWithNext { a, b -> a < b }
                .count { it }
        }
    }
    solution("part2") {
        withInput {
            it.mapNotNull(String::toIntOrNull)
                .windowed(3, transform = List<Int>::sum)
                .zipWithNext { a, b -> a < b }
                .count { it }
        }
    }
})

fun main() = Puzzle.main(Day01)

