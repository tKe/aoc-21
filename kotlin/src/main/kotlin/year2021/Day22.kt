package year2021

import Puzzle
import SolutionCtx
import kotlin.math.max
import kotlin.math.min

fun main() = Puzzle.main(Day22)

object Day22 : Puzzle2021(22) {

    val part1 by solution {
        parseRebootSteps().filterTo(Cuboid(-50..50)).countFinalOn()
    }

    val part2 by solution {
        parseRebootSteps().countFinalOn()
    }

    private fun List<RebootStep>.countFinalOn() = mapWithSubsequent { step, subsequent ->
        step to subsequent.filterTo(step.cuboid).map(RebootStep::cuboid)
    }.filter { (step) -> step.state }.sumOf { (step, overlaps) -> step.cuboid.sizeExcluding(overlaps) }

    private data class Cuboid(val xs: IntRange, val ys: IntRange, val zs: IntRange = 0..0) {
        constructor(cubicRange: IntRange) : this(cubicRange, cubicRange, cubicRange)

        val size = 1L * xs.size * ys.size * zs.size
        fun isEmpty() = xs.isEmpty() || ys.isEmpty() || zs.isEmpty()
        fun intersect(other: Cuboid) = Cuboid(
            xs.intersect(other.xs),
            ys.intersect(other.ys),
            zs.intersect(other.zs)
        )
    }

    private fun IntRange.intersect(other: IntRange) = max(first, other.first)..min(last, other.last)
    private val IntRange.size get() = if (isEmpty()) 0 else 1 + last - first

    private data class RebootStep(val cuboid: Cuboid, val state: Boolean)

    private fun List<RebootStep>.filterTo(cuboid: Cuboid) =
        map { it.copy(cuboid = cuboid.intersect(it.cuboid)) }.filterNot { it.cuboid.isEmpty() }

    private fun Cuboid.sizeExcluding(overlaps: List<Cuboid>): Long =
        size - overlaps.mapWithSubsequent { overlap, subsequent -> intersect(overlap).sizeExcluding(subsequent) }.sum()

    private fun <T, R> List<T>.mapWithSubsequent(transform: (element: T, subsequent: List<T>) -> R) =
        mapIndexed { index: Int, element: T -> transform(element, subList(index + 1, size)) }

    private fun SolutionCtx.parseRebootSteps() = input.map {
        it.split("=", "..", ",").mapNotNull(String::toIntOrNull)
            .chunked(2) { (a, b) -> a..b }
            .let { (x, y, z) -> RebootStep(Cuboid(x, y, z), it.startsWith("on")) }
    }
}
