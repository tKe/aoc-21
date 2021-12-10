package year2021

import Puzzle

object Day10 : Puzzle2021(10, {
    val pairs = mapOf('(' to ')', '{' to '}', '[' to ']', '<' to '>')
    part1 {
        val scores = mapOf(')' to 3, ']' to 57, '}' to 1197, '>' to 25137)
        input.sumOf { line ->
            val stack = ArrayDeque<Char>()
            line.firstOrNull { c ->
                when (val close = pairs[c]) {
                    null -> c != stack.removeFirst()
                    else -> false.also { stack.addFirst(close) }
                }
            }?.let(scores::get) ?: 0
        }
    }

    part2 {
        val scores = mapOf(')' to 1, ']' to 2, '}' to 3, '>' to 4)
        input
            .mapNotNull { line ->
                ArrayDeque<Char>().takeUnless {
                    line.any { c ->
                        when (val close = pairs[c]) {
                            null -> c != it.removeFirst()
                            else -> it.addFirst(close).let { false }
                        }
                    }
                }
            }
            .map { it.fold(0L) { acc, c -> acc * 5 + scores.getValue(c) } }
            .sorted()
            .let { it[it.size / 2] }
    }
})

fun main() = Puzzle.main(Day10)
