package year2021

import Puzzle

fun main() = Puzzle.main(Day20)

object Day20 : Puzzle2021(20) {
    val part1 by solution {
        val algorithm = input.first().asAlgorithm
        val image = Image.fromStrings(input.asSequence().drop(2))
        generateSequence(image) { it.apply(algorithm) }
            .elementAt(2)
            .lightCount
    }

    val part2 by solution {
        val algorithm = input.first().asAlgorithm
        val image = Image.fromStrings(input.asSequence().drop(2))
        generateSequence(image) { it.apply(algorithm) }
            .elementAt(50)
            .lightCount
    }

    private val String.asAlgorithm get() = { idx: Int -> this[idx] == '#' }

    data class IntPair(val x: Int, val y: Int) {
        operator fun plus(other: IntPair) = IntPair(x + other.x, y + other.y)

        companion object {
            fun forRanges(xRange: IntRange, yRange: IntRange = xRange) = sequence {
                for (y in yRange) for (x in xRange) yield(IntPair(x, y))
            }
        }
    }

    class Image(private val pixels: Set<IntPair>, private val outside: Boolean = false) {
        private val xRange = pixels.minOf { it.x }..pixels.maxOf { it.x }
        private val yRange = pixels.minOf { it.y }..pixels.maxOf { it.y }

        val lightCount = pixels.size

        private operator fun contains(coordinate: IntPair) = with(coordinate) { x in xRange && y in yRange }

        operator fun get(coordinate: IntPair) = when (coordinate) {
            !in this -> outside
            in pixels -> true
            else -> false
        }

        fun apply(algorithm: (Int) -> Boolean) = Image(
            pixels = IntPair.forRanges(xRange.expand(1), yRange.expand(1)).filter {
                algorithm(decodeInt(it))
            }.toSet(),
            outside = when (algorithm(0)) {
                true -> !outside
                else -> outside
            }
        )

        override fun toString() = buildString {
            val rows = yRange.expand(by = 1).step(2)
            val cols = xRange.expand(by = 1).step(2)
            val hBorder = "\u2550".repeat(cols.count())
            appendLine("\u2554$hBorder\u2557")
            rows.forEach { y ->
                append('\u2551')
                cols.forEach { x -> append(BLOCK_CHAR[decodeInt(IntPair(x, y), size = 2)]) }
                appendLine('\u2551')
            }
            appendLine("\u255a$hBorder\u255d")
        }

        private fun decodeInt(tl: IntPair, size: Int = 3) =
            IntPair.forRanges(-1 until (size - 1)).map(tl::plus)
                .map { if (get(it)) 1 else 0 }
                .fold(0) { acc, i -> acc shl 1 or i }

        companion object {
            fun fromStrings(lines: Sequence<String>) = Image(lines.flatMapIndexed { y, s ->
                s.mapIndexedNotNull { x, c ->
                    when (c) {
                        '#' -> IntPair(x, y)
                        else -> null
                    }
                }
            }.toSet())
        }
    }

    private fun IntRange.expand(by: Int) = (first - by)..(last + by)

    private const val BLOCK_CHAR = " ▗▖▄▝▐▞▟▘▚▌▙▀▜▛█"
}
