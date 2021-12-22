package year2021

import Puzzle
import SolutionCtx

fun main() = Puzzle.main(Day21)

object Day21 : Puzzle2021(21) {

    val part1 by solution {
        val die = generateSequence { 1..100 }.flatten().iterator()::next
        tailrec fun GameState.play(winningScore: Int = 1000): GameState {
            val played = move(die.roll(3))
            return if (played.prev.score >= winningScore) played
            else played.play(winningScore)
        }
        readGame().play(1000).run { next.score * turn * 3 }
    }

    val part2 by solution {
        val wins = longArrayOf(0, 0)
        generateSequence(mapOf(readGame() to MutableLong(1))) { games ->
            buildMap {
                for ((game, gameCount) in games) {
                    for ((roll, rollCount) in diracRollCounts) {
                        val play = game.move(roll)
                        when {
                            play.prev.score < 21 -> add(play, gameCount * rollCount)
                            else -> wins[play.turn % 2] += gameCount * rollCount
                        }
                    }
                }
            }
        }.first { it.isEmpty() }

        wins.maxOrNull() ?: 0
    }

    private fun SolutionCtx.readGame() = input
        .map { Player(it.split(" ").mapNotNull(String::toIntOrNull).last() - 1) }
        .let { (a, b) -> GameState(a, b) }

    private val diracRollCounts = (1..3).run { flatMap { a -> flatMap { b -> map { a + b + it } } } }
        .groupingBy { it }.fold(0L) { acc, _ -> acc + 1 }.asSequence()

    private data class Player(val position: Int, val score: Int = 0)

    private fun Player.move(roll: Int) = ((position + roll) % 10).let { Player(position = it, score = score + it + 1) }

    private data class GameState(val next: Player, val prev: Player, val turn: Int = 0)

    private fun GameState.move(roll: Int) = GameState(next = prev, prev = next.move(roll), turn = turn + 1)

    private fun (() -> Int).roll(count: Int = 1) = generateSequence { this() }.take(count).sum()
    private data class MutableLong(var value: Long = 0)
    private operator fun MutableLong.times(other: Long) = value * other
    private fun <T> MutableMap<T, MutableLong>.add(key: T, count: Long) { getOrPut(key, ::MutableLong).value += count }
}

