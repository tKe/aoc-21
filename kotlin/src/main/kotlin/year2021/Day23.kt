package year2021

import Puzzle
import SolutionCtx
import java.util.*
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

fun main() = Puzzle.main(Day23)

object Day23 : Puzzle2021(23) {
    val part1 by solution {
        input.readBurrow().organise().cost
    }
    val part2 by solution {
        unfoldedInput.readBurrow().organise().cost
    }

    private fun Burrow.organise(): Burrow {
        val costs = mutableMapOf<Burrow, Int>()
        val queue = PriorityQueue(compareBy(Burrow::cost)).also { it += this }

        while (true) {
            val current = queue.poll()
            if (current.organised) return current
            for (move in current.availableMoves()) {
                val next = current(move)
                if (next.cost < costs.getOrDefault(next, Int.MAX_VALUE)) {
                    costs[next] = next.cost
                    queue += next
                }
            }
        }
    }

    private fun List<String>.readBurrow(): Burrow {
        return Burrow(
            hallway = Burrow.Hallway(this[1]),
            rooms = ('A'..'D').map {
                val idx = (it - 'A') * 2 + 3
                Burrow.Room(it, (2 until (size - 1)).map { r ->
                    this[r][idx]
                }.joinToString(""))
            }, cost = 0
        )
    }

    private val SolutionCtx.unfoldedInput
        get() = input.subList(0, 3) +
                getInput("fold.txt") +
                input.subList(3, input.size)


    class Burrow(private val hallway: Hallway, private val rooms: List<Room>, val cost: Int) {
        private val roomSize = rooms[0].occupants.length
        private val hashCode = hallway.hashCode() * 31 + rooms.hashCode()
        override fun equals(other: Any?) = other is Burrow && hallway == other.hallway && rooms == other.rooms
        override fun hashCode() = hashCode
        override fun toString() =
            buildString {
                appendLine("#".repeat(13))
                appendLine(hallway.occupants)
                appendLine("###${rooms.joinToString("#") { it.occupants[0].toString() }}###")
                repeat(roomSize - 1) { r ->
                    appendLine("  #${rooms.joinToString("#") { it.occupants[r + 1].toString() }}#")
                }
                append("  ${"#".repeat(9)} ($cost)")
            }

        val organised = rooms.all { r -> r.occupants.all { it == r.type } }

        fun availableMoves() = sequence {
            val (inbound, outbound) = rooms.partition { it.isReceiving }
            outbound.forEach { r ->
                val occupantIdx = r.occupants.indexOfFirst { it != '.' }
                val occupant = r.occupants[occupantIdx]
                hallway.availableFrom(r.x).forEach {
                    yield(Move(r.type, occupantIdx, 'H', it, occupant))
                }
                rooms[occupant - 'A'].takeIf { it.isReceiving }?.let {
                    yield(Move(r.type, occupantIdx, r.type, it.openSlot, occupant))
                }
            }
            hallway.occupants.forEachIndexed { idx, occupant ->
                if (occupant != '.') {
                    inbound.singleOrNull { r -> r.type == occupant }?.let { r ->
                        if (hallway.canTraverse(idx, r.x))
                            yield(Move('H', idx, r.type, r.openSlot, occupant))
                    }
                }
            }
        }

        private fun String.setChar(idx: Int, char: Char) = replaceRange(idx..idx, char.toString())
        private fun String.applyMove(move: Move, from: Boolean) =
            if (from) setChar(move.fromLoc, '.') else setChar(move.toLoc, move.type)

        operator fun invoke(move: Move) = Burrow(
            rooms = rooms.map { room ->
                if (room.type == move.fromRoom || room.type == move.toRoom) {
                    room.copy(occupants = room.occupants.applyMove(move, room.type == move.fromRoom))
                } else room
            },
            hallway = if (move.fromRoom == 'H' || move.toRoom == 'H')
                hallway.copy(occupants = hallway.occupants.applyMove(move, 'H' == move.fromRoom))
            else hallway,
            cost = cost + move.cost
        )

        data class Room(val type: Char, val occupants: String) {
            override fun hashCode() = type.hashCode() * 31 + occupants.hashCode()
            override fun equals(other: Any?) = other is Room && type == other.type && occupants == other.occupants

            val x get() = (type - 'A') * 2 + 3
            val isReceiving get() = occupants.all { o -> o == type || o == '.' }
            val openSlot get() = occupants.indexOfLast { it == '.' }
        }

        data class Hallway(val occupants: String) {
            override fun hashCode() = occupants.hashCode()
            override fun equals(other: Any?) = other is Hallway && occupants == other.occupants
        }

        private fun Hallway.availableFrom(roomX: Int) = sequence {
            for (l in (roomX - 1) downTo 0) {
                if (occupants[l] != '.') break
                if (l in usableHallwayCols) yield(l)
            }
            for (l in (roomX + 1)..11) {
                if (occupants[l] != '.') break
                if (l in usableHallwayCols) yield(l)
            }
        }

        private fun Hallway.canTraverse(hallX: Int, roomX: Int) =
            (min(hallX + 1, roomX)..max(hallX - 1, roomX)).all { occupants[it] == '.' }

        data class Move(
            val fromRoom: Char,
            val fromLoc: Int,
            val toRoom: Char,
            val toLoc: Int,
            val type: Char
        ) {
            val cost = when {
                fromRoom == 'H' -> abs(fromLoc - ((toRoom - 'A') * 2 + 3)) + 1 + toLoc
                toRoom == 'H' -> abs(toLoc - ((fromRoom - 'A') * 2 + 3)) + 1 + fromLoc
                else -> abs(toRoom - fromRoom) + toLoc + fromLoc + 2
            } * costs[type - 'A']
        }

        companion object {
            val costs = arrayOf(1, 10, 100, 1000)
            val usableHallwayCols = setOf(1, 2, 4, 6, 8, 10, 11)
        }
    }
}
