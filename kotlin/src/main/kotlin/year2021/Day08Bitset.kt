package year2021

import Puzzle
import SolutionCtx

object Day08Bitset : Puzzle2021(8, {
    fun <R> SolutionCtx.parseInputs(transform: (patterns: List<DigInt>, code: List<DigInt>) -> R) = input.asSequence()
        .map { line -> line.split(" | ").map { it.split(" ").map(String::asDigInt) } }
        .map { (patterns, code) -> transform(patterns, code) }

    part1 {
        val easyDigitLengths = setOf(2, 3, 4, 7)
        parseInputs { _, code -> code.count { it.length in easyDigitLengths } }.sum()
    }

    part2 {
        parseInputs { patterns, code -> patterns.createDecoder() decode code }.sum()
    }
})

@JvmInline
private value class DigInt(val value: Int) {
    constructor(repr: String) : this(repr.fold(0) { acc, c -> acc or (1 shl (c - 'a')) })
    val length get() = value.countOneBits()
    val segments get() = sequenceOf(a, b, c, d, e, f, g).filter { it in this }
    override fun toString() = "DigInt(${value.toString(2)}:${segments.map { 'a' + it.countTrailingZeroBits() }.joinToString("")})"
    operator fun contains(seg: Int) = value and seg != 0
    companion object Segment {
        const val a = 0b1
        const val b = 0b10
        const val c = 0b100
        const val d = 0b1000
        const val e = 0b10000
        const val f = 0b100000
        const val g = 0b1000000
    }
}

private infix fun Map<Int, Int>.decode(digit: DigInt) = DigInt(digit.segments.fold(0) { acc, i -> acc or getValue(i) })
private infix fun Map<Int, Int>.decode(code: List<DigInt>) =
    code.map { digits.indexOf(decode(it)) }.fold(0) { acc, i -> 10 * acc + i }

private fun String.asDigInt() = DigInt(this)

private val digits = listOf("abcefg", "cf", "acdeg", "acdfg", "bcdf", "abdfg", "abdefg", "acf", "abcdefg", "abcdfg")
    .map(String::asDigInt)

private fun List<DigInt>.createDecoder() =
    flatMap(DigInt::segments).groupingBy { it }.eachCount().mapValues { (seg, count) ->
        when (count) {
            4 -> DigInt.e
            6 -> DigInt.b
            7 -> if (seg in single { it.length == 4 }) DigInt.d else DigInt.g
            8 -> if (seg in single { it.length == 2 }) DigInt.c else DigInt.a
            9 -> DigInt.f
            else -> error("unexpected segment count $count for input segment $seg")
        }
    }

fun main() = Puzzle.main(Day08Bitset)
