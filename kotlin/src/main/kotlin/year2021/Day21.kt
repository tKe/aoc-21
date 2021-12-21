package year2021

import Puzzle
import SolutionCtx

fun main() = Puzzle.main(Day21)

object Day21 : Puzzle2021(21) {
    val part1 by solution {
        val die = deterministicD100.counting()
        val (playerOne, playerTwo) = readPlayers()
        val loser = generateSequence(GameState(playerOne, playerTwo, winningScore = 1000)) {
            it.move(die.roll(3))
        }.firstNotNullOf { it.loser }

        loser.score * die.rollCount
    }

    val part2 by solution {
        val (playerOne, playerTwo) = readPlayers()

        val initialGame = GameState(playerOne, playerTwo, winningScore = 21)
        val wins = generateSequence(mapOf(initialGame to 1L) to emptyMap<Int, Long>()) { (games, wins) ->
            val nextGames = mutableMapOf<GameState, Long>()
            val nextWins = wins.toMutableMap()
            games.forEach { (game, gameCount) ->
                diracRollToUniverse.forEach { (roll, rollCount) ->
                    val nextGame = game.move(roll)
                    val winner = nextGame.winner
                    if (winner == null) {
                        nextGames.compute(nextGame) { _, prevCount -> (prevCount ?: 0L) + rollCount * gameCount }
                    } else {
                        nextWins.compute(winner.number) { _, prevWins -> (prevWins ?: 0L) + rollCount * gameCount }
                    }
                }
            }
            nextGames to nextWins
        }.firstNotNullOf { (games, wins) -> wins.takeIf { games.isEmpty() } }

        wins.values.maxOf { it }
    }

    private fun SolutionCtx.readPlayers() = input.map {
        val (player, position) = it.split(" ").mapNotNull(String::toIntOrNull)
        Player(player, position)
    }

    private val diracRollToUniverse = (1..3).flatMap { a -> (1..3).flatMap { b -> (1..3).map { a + b + it } } }
        .groupingBy { it }.eachCount().mapValues { it.value.toLong() }

    private data class Player(val number: Int, val position: Int, val score: Int = 0) {
        fun move(roll: Int) = ((position + roll - 1) % 10 + 1).let { position ->
            copy(position = position, score = score + position)
        }
    }

    private data class GameState(val playerOne: Player, val playerTwo: Player, val turn: Int = 0, val winningScore: Int) {
        fun move(roll: Int) = when (turn % 2) {
            0 -> copy(playerOne = playerOne.move(roll), turn = turn + 1)
            else -> copy(playerTwo = playerTwo.move(roll), turn = turn + 1)
        }

        val winner by lazy {
            playerOne.takeIf { it.score >= winningScore } ?: playerTwo.takeIf { it.score >= winningScore }
        }
        val loser by lazy {
            when (winner) {
                playerOne -> playerTwo
                playerTwo -> playerOne
                else -> null
            }
        }
    }

    fun interface Die : () -> Int
    interface CountingDie : Die {
        val rollCount: Int
    }

    private fun Die.roll(count: Int = 1) = generateSequence { this() }.take(count).sum()

    private fun Die.counting() = object : CountingDie {
        override var rollCount: Int = 0
        override fun invoke() = this@counting().also { rollCount++ }
    }

    private val deterministicD100: Die
        get() = sequence { while (true) yieldAll(1..100) }.iterator().let { Die { it.next() } }
}
