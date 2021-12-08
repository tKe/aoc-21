package year2021

import Puzzle
import SolutionCtx

object Day08 : Puzzle2021(8, {
    fun <R> SolutionCtx.parseInputs(transform: (patterns: List<String>, code: List<String>) -> R) = input.asSequence()
        .map { line -> line.split(" | ").map { it.split(" ") } }
        .map { (patterns, code) -> transform(patterns, code) }

    part1 {
        val easyDigitLengths = setOf(2, 3, 4, 7)
        parseInputs { _, code -> code.count { it.length in easyDigitLengths } }.sum()
    }

    part2 {
        parseInputs { patterns, code -> patterns.createDecoder() decode code }.sum()
    }
})

private infix fun Map<Char, Char>.decode(code: List<String>) =
    code.map { digits.indexOf(it.map(::getValue).toSet()) }.fold(0) { acc, i -> 10 * acc + i }

private val digits = listOf("abcefg", "cf", "acdeg", "acdfg", "bcdf", "abdfg", "abdefg", "acf", "abcdefg", "abcdfg")
    .map(String::toSet)

private fun List<String>.createDecoder() =
    flatMap(String::asSequence).groupingBy { it }.eachCount().mapValues { (seg, count) ->
        when (count) {
            4 -> 'e'
            6 -> 'b'
            7 -> if (seg in single { it.length == 4 }) 'd' else 'g'
            8 -> if (seg in single { it.length == 2 }) 'c' else 'a'
            9 -> 'f'
            else -> error("unexpected segment count $count for input segment $seg")
        }
    }

fun main() = Puzzle.main(Day08)
