package year2021

import Puzzle

fun main() = Puzzle.main(Day18)

object Day18 : Puzzle2021(18, {
    part1 {
        Day18.sumAll(input).magnitude
    }

    part2 {
        input.flatMap { a -> (input - a).map { b -> SnailfishNumber(a) + SnailfishNumber(b) } }
            .maxOf { it.magnitude }
    }
}) {
    internal fun sumAll(numbers: List<String>) = numbers.map { SnailfishNumber(it) }
        .reduce { acc, n -> acc + n }
}

@JvmInline
internal value class SnailfishNumber(val value: String) {
    override fun toString() = value
    internal val magnitude: Int get() {
        val simplePair = """\[(\d+),(\d+)]""".toRegex()
        return generateSequence(value) {
            simplePair.replace(it) { match ->
                val (left, right) = match.destructured
                (left.toInt() * 3 + right.toInt() * 2).toString()
            }
        }.first { !simplePair.containsMatchIn(it) }.toInt()
    }
}

private operator fun SnailfishNumber.plus(other: SnailfishNumber) = SnailfishNumber("[$this,$other]").reduce()

private fun SnailfishNumber.explodeIndexOrNull() = value.asSequence().runningFold(0) { depth, c ->
    depth + when (c) {
        '[' -> 1
        ']' -> -1
        else -> 0
    }
}.withIndex().firstNotNullOfOrNull { (index, depth) -> index.takeIf { depth > 4 } }

private fun SnailfishNumber.explode(idx: Int): SnailfishNumber {
    val splitLeft = value.toCharArray(0, idx - 1).also {
        val i = it.indexOfLast { c -> c != '[' && c != ']' && c != ',' }
        if(i != -1) it[i] = (value[idx].snInt + it[i].snInt).snChar
    }

    val splitRight = value.toCharArray(idx + 4).also {
        val i = it.indexOfFirst { c -> c != '[' && c != ']' && c != ',' }
        if(i != -1) it[i] = (value[idx + 2].snInt + it[i].snInt).snChar
    }

    return SnailfishNumber(buildString {
        append(splitLeft)
        append('0')
        append(splitRight)
    })
}

private fun SnailfishNumber.splitIndexOrNull() = value.indexOfFirst {
    when (it) {
        '[', ']', ',' -> false
        else -> it.snInt >= 10
    }
}.takeUnless { it == -1 }

private fun SnailfishNumber.split(idx: Int) = value[idx].snInt.let { v ->
    val left = v / 2
    val right = v - left
    value.replaceRange(idx, idx + 1, "[${left.snChar},${right.snChar}]")
}.let(::SnailfishNumber)

private fun SnailfishNumber.reduce() = generateSequence(this) { num ->
    num.explodeIndexOrNull()?.let(num::explode)
        ?: num.splitIndexOrNull()?.let(num::split)
}.last()

// horrible hack to keep literals > 9 to a single character - which keeps explode/split indexing simpler
private val Int.snChar: Char get() = if (this < 36) digitToChar(36) else '~' + this
private val Char.snInt: Int get() = if (this > '~') this - '~' else digitToInt(36)
