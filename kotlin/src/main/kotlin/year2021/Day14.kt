package year2021

import Puzzle
import SolutionCtx

fun main() = Puzzle.main(Day14)

object Day14 : Puzzle2021(14, {

    fun <T, K> Grouping<T, K>.eachSum(transform: (T) -> Long = { 1L }): Map<K, Long> =
        fold(0L) { sum, element -> sum + transform(element) }

    fun Map<String, List<String>>.polymerise(counts: Map<String, Long>): Map<String, Long> =
        counts.asSequence().flatMap { (pair, count) ->
            this[pair]?.map { it to count } ?: emptyList()
        }.groupingBy { it.first }.eachSum { it.second }

    fun <K, V: Any> Map<K, V>.merge(other: Map<K, V>, combine: (V, V) -> V) = (keys + other.keys).associateWith {
        val a = this[it]
        val b = other[it]
        if (a == null || b == null) a ?: b!! // must exist in one or t'other
        else combine(a, b)
    }

    fun Map<String, Long>.toCharCounts(): Map<Char, Long> {
        val firsts = asSequence().groupingBy { it.key[0] }.eachSum { it.value }
        val seconds = asSequence().groupingBy { it.key[1] }.eachSum { it.value }
        return firsts.merge(seconds, ::maxOf)
    }

    fun SolutionCtx.polymerise(
        iterations: Int,
    ): Long {
        val template = input.first()
        val rules = input.asSequence().drop(2).map { it.split(" -> ").let { (a, b) -> a to b.first() } }.toMap()

        val pairMappings = rules.mapValues { (k, v) -> listOf("${k[0]}$v", "$v${k[1]}") }
        val initialPairCounts = template.windowedSequence(size = 2, step = 1).groupingBy { it }.eachSum()
        val pairCounts = generateSequence(initialPairCounts, pairMappings::polymerise).drop(iterations).first()
        val charCounts = pairCounts.toCharCounts()
        return charCounts.maxOf { it.value } - charCounts.minOf { it.value }
    }

    part1 {
        polymerise(10)
    }

    part2 {
        polymerise(40)
    }
})
