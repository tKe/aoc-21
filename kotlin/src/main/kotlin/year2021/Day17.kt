package year2021

import Puzzle
import SolutionCtx
import kotlin.math.sign

fun main() = Puzzle.main(Day17)

object Day17 : Puzzle2021(17, {

    data class ProbeState(val x: Int, val y: Int, val vx: Int, val vy: Int)

    fun ProbeState.move() = ProbeState(x = x + vx, y = y + vy, vx = vx - vx.sign, vy = vy - 1)

    data class Target(val x: IntRange, val y: IntRange)

    fun Target.possibleXVelocities() = (1..x.last).asSequence()
    fun Target.possibleYVelocities() = (y.first until -y.first).asSequence()
    fun Target.possibleLaunches() = possibleXVelocities().flatMap { possibleYVelocities().map(it::to) }

    fun String.readTargetArea() = split("=", ", ", "..").mapNotNull(String::toIntOrNull)
        .let { (x1, x2, y1, y2) -> Target(x1..x2, y1..y2) }

    fun <R> SolutionCtx.readTarget(block: Target.() -> R) = input.first().readTargetArea().block()

    fun launch(vx: Int, vy: Int, x: Int = 0, y: Int = 0) = generateSequence(ProbeState(x, y, vx, vy), ProbeState::move)
    infix fun Sequence<ProbeState>.at(target: Target) = takeWhile { it.x <= target.x.last && it.y >= target.y.first }
    infix fun Sequence<ProbeState>.hit(target: Target) = any { it.x in target.x && it.y in target.y }

    part1 {
        readTarget {
            possibleLaunches()
                .map { (vx, vy) -> launch(vx, vy) at this }
                .filter { it hit this }
                .maxOf { it.maxOf(ProbeState::y) }
        }
    }

    part2 {
        readTarget {
            possibleLaunches()
                .map { (vx, vy) -> launch(vx, vy) at this }
                .count { it hit this }
        }
    }
})
