package year2021

import Puzzle

object Day03 : Puzzle2021(3, {
    fun Int.inv(bits: Int) = xor((-1 shl bits).inv())

    part1 {
        val length = input.first().length
        val ones = IntArray(length)
        input.forEach { line ->
            line.forEachIndexed { index, c ->
                if (c == '1') ones[index]++
            }
        }

        val half = input.size / 2
        val gammaRate = ones.indices.fold(0) { acc, i ->
            acc or if (ones[i] > half) 1 shl (length - 1 - i) else 0
        }
        val epsilonRate = gammaRate.inv(length)

        gammaRate * epsilonRate
    }

    part2 {
        fun List<String>.decode(predicate: (ones: Int, zeros: Int) -> Boolean): Int = toMutableList().run {
            val length = first().length
            for (idx in 0 until length) {
                val ones = count { it[idx] == '1' }
                val keep = if (predicate(ones, size - ones)) '1' else '0'
                removeAll { it[idx] != keep }
                if (size == 1) break
            }
            single().toInt(2)
        }

        val oxygenGeneratorRating = input.decode { ones, zeros -> ones >= zeros }
        val co2ScrubberRating = input.decode { ones, zeros -> ones < zeros }

        oxygenGeneratorRating * co2ScrubberRating
    }
})


fun main() = Puzzle.main(Day03)
