package year2021

import Puzzle
import SolutionCtx

fun main() = Puzzle.main(Day24Nomad)

object Day24Nomad : Puzzle2021(24) {
    val part1 by solution {
        parseNomad().solve(9 downTo 1)
    }

    val part2 by solution {
        parseNomad().solve(1..9)
    }

    private val chunkPattern =
        """inp w;mul x 0;add x z;mod x 26;div z (-?\d+);add x (-?\d+);eql x w;eql x 0;mul y 0;add y 25;mul y x;add y 1;mul z y;mul y 0;add y w;add y (-?\d+);mul y x;add z y""".toRegex()

    private fun SolutionCtx.parseNomad(): Step {
        val chunks = sequence {
            yield(0)
            yieldAll(input.mapIndexedNotNull { i, s -> i.takeIf { s.startsWith("inp") } }.drop(1))
            yield(input.size)
        }.zipWithNext(input::subList).toList()

        return chunks.map {
            val chunkLine = it.joinToString(";")
            val (a, b, c) = chunkPattern.matchEntire(chunkLine)?.destructured ?: error("didn't match? [$chunkLine]")
            Chunk(a.toInt(), b.toInt(), c.toInt())
        }.foldRightIndexed(null as Step?) { stepIdx, chunk, next ->
            Step(stepIdx, chunk, next)
        } ?: error("no steps in program?")
    }

    data class Chunk(val a: Int, val b: Int, val c: Int) {
        operator fun invoke(input: Int, z: Int = 0): Int {
            val x = if (input != ((z % 26) + b)) 1 else 0
            return (z / a) * (25 * x + 1) + (input + c) * x
        }
    }

    data class Step(val id: Int, val chunk: Chunk, val next: Step? = null)

    fun Step.invoke(input: Int, z: Int): Int = chunk(input, z)

    private fun Step.solve(
        inputProgression: IntProgression,
        z: Int = 0,
        cache: MutableMap<Any, Long> = mutableMapOf()
    ): Long = cache.getOrPut(id to z) {
        (when (chunk.a) {
            26 -> listOf(z % 26 + chunk.b).filter { it in 1..9 }
            else -> inputProgression
        }).firstNotNullOfOrNull { digit ->
            when (next) {
                null -> digit.takeIf { invoke(digit, z) == 0 }?.toLong()
                else -> {
                    val nextZ = invoke(digit, z)
                    next.solve(inputProgression, nextZ, cache).takeIf { it != -1L }
                        ?.let { "$digit$it".toLong() }
                }
            }
        } ?: -1L
    }
}
