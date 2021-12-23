package year2021

import Puzzle
import SolutionCtx
import java.util.*
import kotlin.math.max
import kotlin.math.min

fun main() = Puzzle.main(Day22CoordCompression)

object Day22CoordCompression : Puzzle2021(22) {

    val part1 by solution {
        parseInputs(-50..50).countLit()
    }

    val part2 by solution {
        parseInputs().countLit()
    }

    private fun List<Instruction>.countLit() =
        CompressedBitSet3(extractAxis { x }, extractAxis { y }, extractAxis { z })
            .also { forEach { (state, x, y, z) -> it[x, y, z] = state } }
            .cardinality()

    class CompressedBitSet3(xValues: Iterable<Int>, yValues: Iterable<Int>, zValues: Iterable<Int>) {
        private val xAxis = CompressedAxis(xValues)
        private val yAxis = CompressedAxis(yValues)
        private val zAxis = CompressedAxis(zValues)
        private val data = Array(xAxis.size) { Array(yAxis.size) { BitSet(zAxis.size) } }

        operator fun get(x: Int, y: Int, z: Int) = data[xAxis[x]][yAxis[y]][zAxis[z]]
        operator fun set(x: IntRange, y: IntRange, z: IntRange, value: Boolean) = with(zAxis[z]) {
            for (xi in xAxis[x]) for (yi in yAxis[y]) data[xi][yi].set(first, last + 1, value)
        }

        fun cardinality(): Long {
            var sum = 0L
            for (x in 0 until xAxis.size) for (y in 0 until yAxis.size) {
                val m = xAxis.sizes[x].toLong() * yAxis.sizes[y]
                data[x][y].stream().forEach { z -> sum += m * zAxis.sizes[z] }
            }
            return sum
        }

        private class CompressedAxis private constructor(val sizes: IntArray, private val lookup: Map<Int, Int>) {
            val size = sizes.size
            operator fun get(r: Int) = lookup[r] ?: error("unknown raw value $r")
            operator fun get(r: IntRange) = get(r.first) until get(r.last + 1)

            companion object {
                operator fun invoke(raw: Iterable<Int>) = raw.sorted().distinct().let {
                    CompressedAxis(
                        sizes = it.zipWithNext { a, b -> b - a }.toIntArray(),
                        lookup = it.withIndex().associate { (idx, value) -> value to idx }
                    )
                }
            }
        }
    }

    data class Instruction(val state: Boolean, val x: IntRange, val y: IntRange, val z: IntRange)

    private inline fun List<Instruction>.extractAxis(crossinline axis: Instruction.() -> IntRange) =
        flatMap { it.axis().run { sequenceOf(first, last + 1) } }

    private fun SolutionCtx.parseInputs(range: IntRange = Int.MIN_VALUE..Int.MAX_VALUE): List<Instruction> =
        input.map { line ->
            line.split("=", "..", ",").mapNotNull(String::toIntOrNull)
                .chunked(2).map { (a, b) -> max(a, range.first)..min(b, range.last) }
                .let { (x, y, z) -> Instruction(line.startsWith("on"), x, y, z) }
        }.filterNot { it.x.isEmpty() || it.y.isEmpty() || it.z.isEmpty() }
}
