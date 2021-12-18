package year2021

import Puzzle
import SolutionCtx
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sign

fun main() = Puzzle.main(Day17)

object Day17 : Puzzle2021(17, {

    data class ProbeState(val x: Int, val y: Int, val vx: Int, val vy: Int)
    data class Target(val x: IntRange, val y: IntRange)

    fun String.readTargetArea() = split("=", ", ", "..")
        .mapNotNull(String::toIntOrNull)
        .let { (x1, x2, y1, y2) -> Target(x1..x2, y1..y2) }

    fun SolutionCtx.readTarget() = input.first().readTargetArea()

    fun ProbeState.move() = ProbeState(x = x + vx, y = y + vy, vx = vx - vx.sign, vy = vy - 1)

    fun launch(vx: Int, vy: Int, x: Int = 0, y: Int = 0) = generateSequence(ProbeState(x, y, vx, vy), ProbeState::move)

    infix fun Sequence<ProbeState>.at(target: Target) = takeWhile { !(it.x > target.x.last || it.y < target.y.first) }.toList()

    infix fun List<ProbeState>.hit(target: Target) = any { it.x in target.x && it.y in target.y }

    @Suppress("unused")
    fun print(path: List<ProbeState>, target: Target) {
        val areaX = min(target.x.first, path.minOf { it.x })..max(target.x.last, path.maxOf { it.x })
        val areaY = min(target.y.first, path.minOf { it.y })..max(target.y.last, path.maxOf { it.y })
        Array(areaY.count()) { CharArray(areaX.count()) { '.' } }.apply {
            target.y.forEach { y -> target.x.forEach { x -> this[y - areaY.first][x - areaX.first] = 'T' } }
            path.forEach { this[it.y - areaY.first][it.x - areaX.first] = '#' }
            path.first().let { this[it.y - areaY.first][it.x - areaX.first] = 'S' }
            reverse()
        }.forEach { println(it.concatToString()) }
    }

    fun Target.possibleXVelocities() = (1..x.last).asSequence()
        .filter { (it downTo 0).asSequence().runningReduce(Int::plus).any(x::contains) }

    fun Target.possibleYVelocities() = (y.first until -y.first).asSequence()
        .filter { vy ->
            (vy downTo Int.MIN_VALUE).asSequence()
                .runningReduce(Int::plus)
                .takeWhile { it >= y.first }
                .any { it in y }
        }

    fun Target.possibleLaunches() = possibleXVelocities().flatMap { possibleYVelocities().map(it::to) }

    part1 {
        val target = readTarget()
        val path = target.possibleLaunches()
            .map { (vx, vy) -> launch(vx, vy) at target }
            .filter { it hit target }
            .maxByOrNull {
                it.maxOf { p -> p.y }
            } ?: error("no hits")

        path.maxOf { it.y }
    }

    part2 {
        val target = readTarget()
        target.possibleLaunches()
            .map { (vx, vy) -> launch(vx, vy) at target }
            .count { it hit target }
    }
})
