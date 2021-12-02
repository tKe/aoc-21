package year2021

import Puzzle

object Day02 : Puzzle2021(2, {
    data class Location(val horizontalPosition: Int = 0, val depth: Int = 0, val aim: Int = 0)
    fun Sequence<String>.parseCommands() =
        map { it.split(' ', limit = 2) }
            .map { (cmd, v) -> cmd to v.toInt() }
    fun Sequence<Pair<String, Int>>.drive(engine: Location.(String, Int) -> Location) =
        fold(Location()) { location, (command, amount) -> location.engine(command, amount) }

    solution("part1") {
        withInput { lines ->
            lines.parseCommands()
                .drive { cmd, v ->
                    when(cmd) {
                        "up" -> copy(depth = depth - v)
                        "down" -> copy(depth = depth + v)
                        "forward" -> copy(horizontalPosition = horizontalPosition + v)
                        else -> this
                    }
                }
                .let { (a, b) -> a * b }
        }
    }

    solution("part2") {
        withInput { lines ->
            lines.parseCommands()
                .drive { cmd, v ->
                    when(cmd) {
                        "up" -> copy(aim = aim - v)
                        "down" -> copy(aim = aim + v)
                        "forward" -> copy(
                            horizontalPosition = horizontalPosition + v,
                            depth = depth + (aim * v)
                        )
                        else -> this
                    }
                }
                .let { (a, b) -> a * b }
        }
    }
})

fun main() = Puzzle.main(Day02)
