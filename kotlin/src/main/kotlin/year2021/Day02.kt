package year2021

import Puzzle

private data class Location(val horizontalPosition: Int = 0, val depth: Int = 0, val aim: Int = 0)
private typealias Mover = Location.(Int) -> Location

object Day02 : Puzzle2021(2, {
    fun List<String>.drive(up: Mover, down: Mover, forward: Mover) = asSequence()
        .map { it.split(' ', limit = 2) }
        .map { (direction, amount) ->
            when (direction) {
                "up" -> up
                "down" -> down
                "forward" -> forward
                else -> error("unhandled direction [$direction]")
            } to amount.toInt()
        }
        .fold(Location()) { location, (move, amount) -> location.move(amount) }
        .let { (a, b) -> a * b }

    part1 {
        input.drive(
            up = { copy(depth = depth - it) },
            down = { copy(depth = depth + it) },
            forward = { copy(horizontalPosition = horizontalPosition + it) }
        )
    }

     part2 {
        input.drive(
            up = { copy(aim = aim - it) },
            down = { copy(aim = aim + it) },
            forward = {
                copy(
                    horizontalPosition = horizontalPosition + it,
                    depth = depth + (aim * it)
                )
            }
        )
    }
})

fun main() = Puzzle.main(Day02)
