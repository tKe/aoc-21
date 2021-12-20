package year2021

import Puzzle
import SolutionCtx
import kotlin.math.abs

fun main() = Puzzle.main(Day19)

object Day19 : Puzzle2021(19) {
    val part1 by solution {
        readScanners().resolveLocations()
            .flatMap { it.beacons }
            .distinct().size
    }

    val part2 by solution {
        val scanners = readScanners()
        val scannerLocations = scanners.resolveLocations().map { it.location }

        scannerLocations.maxOf { from ->
            (scannerLocations - from).maxOf { from manhattanDistanceTo it }
        }
    }

    data class Scanner(val location: Pos3d, val beacons: Set<Pos3d>) {
        val signatures = beacons.combinations { a, b ->
            setOf(abs(a.x - b.x), abs(a.y - b.y), abs(a.z - b.z)) to setOf(a, b)
        }.toMap()
    }

    private fun List<Scanner>.resolveLocations() =
        generateSequence(take(1) to drop(1)) { (known, unknown) ->
            val newlyFound = mutableListOf<Scanner>()
            val stillUnknown = unknown.filter { target ->
                known.firstNotNullOfOrNull { it.tryLocating(target) }
                    ?.also { newlyFound += it } == null
            }
            require(newlyFound.isNotEmpty()) { "No progress made" }
            (known + newlyFound) to stillUnknown
        }.firstNotNullOf { (known, unknown) -> known.takeIf { unknown.isEmpty() } }

    private fun Scanner.candidateOverlaps(other: Scanner) =
        signatures.asSequence().mapNotNull { (k, v) -> other.signatures[k]?.let(v::to) }

    private fun Scanner.tryLocating(other: Scanner): Scanner? {
        return candidateOverlaps(other).flatMap { (srcPair, targetPair) ->
            rotations.asSequence().flatMap { rotation ->
                val rotatedTarget = targetPair.map(rotation)
                rotatedTarget.possibleDeltasTo(srcPair)
                    .filter { delta -> rotatedTarget.map { it - delta }.all(srcPair::contains) }
                    .map { it to other.beacons.map(rotation).map { p -> p - it } }
                    .filter { (_, newBeacons) -> newBeacons.count(beacons::contains) >= 12 }
            }
        }.firstOrNull()
            ?.let { (delta, newBeacons) -> Scanner(delta, newBeacons.toSet()) }
    }

    private fun SolutionCtx.readScanners() = buildList {
        val lines = input.iterator()
        while (lines.hasNext()) {
            val scannerLines = lines.asSequence().takeWhile { it.isNotEmpty() }.toList()
            add(Scanner(Pos3d(0, 0, 0), scannerLines.drop(1).map {
                it.split(",")
                    .map(String::toInt)
                    .let { (x, y, z) -> Pos3d(x, y, z) }
            }.toSet()))
        }
    }

    data class Pos3d(val x: Int, val y: Int, val z: Int) {
        operator fun minus(other: Pos3d) = Pos3d(x - other.x, y - other.y, z - other.z)
        operator fun plus(other: Pos3d) = Pos3d(x + other.x, y + other.y, z + other.z)
        infix fun manhattanDistanceTo(other: Pos3d) = abs(x - other.x) + abs(y - other.y) + abs(z - other.z)
    }

    private val rotations = listOf<Pos3d.() -> Pos3d>(
        { this },
        { Pos3d(x = z, y = y, z = -x) },  // roll
        { Pos3d(x = -y, y = z, z = -x) }, // turn-cw
        { Pos3d(x = -z, y = -y, z = -x) },// turn-cw
        { Pos3d(x = y, y = -z, z = -x) }, // turn-cw
        { Pos3d(x = -x, y = -z, z = -y) },// roll
        { Pos3d(x = -z, y = x, z = -y) }, // turn-ccw
        { Pos3d(x = x, y = z, z = -y) },  // turn-ccw
        { Pos3d(x = z, y = -x, z = -y) },// turn-ccw
        { Pos3d(x = -y, y = -x, z = -z) },// roll
        { Pos3d(x = x, y = -y, z = -z) }, // turn-cw
        { Pos3d(x = y, y = x, z = -z) },  // turn-cw
        { Pos3d(x = -x, y = y, z = -z) }, // turn-cw
        { Pos3d(x = -z, y = y, z = x) },  // roll
        { Pos3d(x = y, y = z, z = x) },   // turn-ccw
        { Pos3d(x = z, y = -y, z = x) },  // turn-ccw
        { Pos3d(x = -y, y = -z, z = x) }, // turn-ccw
        { Pos3d(x = x, y = -z, z = y) },  // roll
        { Pos3d(x = z, y = x, z = y) },   // turn-cw
        { Pos3d(x = -x, y = z, z = y) },  // turn-cw
        { Pos3d(x = -z, y = -x, z = y) }, // turn-cw
        { Pos3d(x = y, y = -x, z = z) },  // roll
        { Pos3d(x = -x, y = -y, z = z) }, // turn-ccw
        { Pos3d(x = -y, y = x, z = z) },  // turn-ccw
    )

    private fun Iterable<Pos3d>.possibleDeltasTo(other: Iterable<Pos3d>) =
        asSequence().flatMap { source -> other.asSequence().map { source - it } }

    private fun <T, R> Iterable<T>.combinations(transform: (T, T) -> R) = asSequence().withIndex().flatMap { (idx, a) ->
        asSequence().drop(idx + 1).map { transform(a, it) }
    }

}
