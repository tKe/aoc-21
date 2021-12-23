package year2021

import Puzzle
import SolutionCtx
import java.util.*

fun main() = Puzzle.main(Day22CoordCompression)

object Day22CoordCompression : Puzzle2021(22) {

    val part1 by solution {
        parseInputs().countLit { it in -50..50 }
    }

    val part2 by solution {
        parseInputs().countLit()
    }

    private fun List<Instruction>.countLit(filter: (Int) -> Boolean = { true }): Long {
        val (xs, xLookup) = extractAxisLookup(Instruction::x, filter)
        val (ys, yLookup) = extractAxisLookup(Instruction::y, filter)
        val (zs, zLookup) = extractAxisLookup(Instruction::z, filter)

        val grid = Array(xs.size) { Array(ys.size) { BooleanArray(zs.size) } }
        grid.process(this, xLookup, yLookup, zLookup)
        return grid.extract(xs, ys, zs)
    }

    private fun Array<Array<BooleanArray>>.extract(xs: List<Int>, ys: List<Int>, zs: List<Int>): Long {
        val xr = xs.zipWithNext { a, b -> b - a }.withIndex()
        val yr = ys.zipWithNext { a, b -> b - a }.withIndex().toList()
        val zr = zs.zipWithNext { a, b -> b - a }.withIndex().toList()
        return xr.sumOf { (x, xCnt) ->
            yr.sumOf { (y, yCnt) ->
                zr.sumOf { (z, zCnt) ->
                    (if (this[x][y][z]) zCnt else 0L).toLong()
                } * yCnt
            } * xCnt
        }
    }

    private fun Array<Array<BooleanArray>>.process(
        instructions: List<Instruction>, xMap: SortedMap<Int, Int>, yMap: SortedMap<Int, Int>,
        zMap: SortedMap<Int, Int>
    ) {
        for (instruction in instructions) {
            for (x in xMap[instruction.x]) {
                for (y in yMap[instruction.y]) {
                    for (z in zMap[instruction.z]) {
                        this[x][y][z] = instruction.state
                    }
                }
            }
        }
    }

    data class Instruction(val x: IntRange, val y: IntRange, val z: IntRange, val state: Boolean)

    private inline fun List<Instruction>.extractAxisLookup(
        crossinline extractor: Instruction.() -> IntRange,
        filter: (Int) -> Boolean
    ) = extractAxis(extractor, filter).let { it to it.createLookup() }

    private inline fun List<Instruction>.extractAxis(
        crossinline extractor: Instruction.() -> IntRange,
        filter: (Int) -> Boolean
    ) = flatMap { it.extractor().run { sequenceOf(first, last + 1) } }.filter(filter).distinct().sorted()

    private fun List<Int>.createLookup() = mapIndexed { idx, value -> value to idx }.toMap().toSortedMap()

    private operator fun SortedMap<Int, Int>.get(keyRange: IntRange) =
        subMap(keyRange.first, keyRange.last + 1).values.asSequence()

    private fun SolutionCtx.parseInputs(): List<Instruction> = input.map {
        it.split("=", "..", ",").mapNotNull(String::toIntOrNull)
            .chunked(2).map { (a, b) -> a..b }
            .let { (x, y, z) -> Instruction(x, y, z, it.startsWith("on")) }
    }
}
