package year2021

import Puzzle

fun main() = Puzzle.main(Day16)

object Day16 : Puzzle2021(16, {
    part1 {
        input.map { line ->
            BITSPacket.readFrom(line.biterator()).walk().sumOf { it.version }
        }
    }

    part2 {
        input.map { line ->
            BITSPacket.readFrom(line.biterator()).value
        }
    }
})

private interface Biterator : Iterator<Boolean> {
    val remaining: Int
    fun next(bits: Int): Int
    override fun hasNext() = remaining > 0
    override fun next() = next(1) == 1
}

private fun Biterator.readVarint(): Long = sequence {
    while (true) {
        val next = next(5).toLong()
        yield(next and 0b1111)
        if (next and 0b10000 == 0) break
    }
}.fold(0L) { acc, i -> acc shl 4 or i }

private fun Biterator.limit(n: Int) = object : Biterator {
    var consumed = 0
    override val remaining get() = n - consumed

    override fun next(bits: Int): Int {
        require(bits <= remaining)
        return this@limit.next(bits).also { consumed += bits }
    }
}

private fun Biterator.readPackets() = sequence { while(remaining >= 11) yield(readPacket()) }
private fun Biterator.readPacket() = BITSPacket.readFrom(this)

private sealed interface BITSPacket {
    val version: Int
    val type: Int

    val value: Long

    data class Literal(override val version: Int, override val type: Int, override val value: Long) : BITSPacket
    sealed class Operator(override val version: Int, override val type: Int, val packets: List<BITSPacket>) : BITSPacket {
        abstract class ReducingOperator(version: Int, type: Int, packets: List<BITSPacket>, reducer: (Long, Long) -> Long) :
            Operator(version, type, packets) {
            override val value = packets.map { it.value }.reduce(reducer)
        }

        abstract class ComparisonOperator(
            version: Int,
            type: Int,
            packets: List<BITSPacket>,
            operator: (Long, Long) -> Boolean
        ) : Operator(version, type, packets) {
            override val value = packets.let { (a, b) -> if (operator(a.value, b.value)) 1L else 0L }
        }

        class Sum(version: Int, type: Int, packets: List<BITSPacket>) :
            ReducingOperator(version, type, packets, Long::plus)

        class Product(version: Int, type: Int, packets: List<BITSPacket>) :
            ReducingOperator(version, type, packets, Long::times)

        class Min(version: Int, type: Int, packets: List<BITSPacket>) : ReducingOperator(version, type, packets, ::minOf)
        class Max(version: Int, type: Int, packets: List<BITSPacket>) : ReducingOperator(version, type, packets, ::maxOf)
        class GreaterThan(version: Int, type: Int, packets: List<BITSPacket>) :
            ComparisonOperator(version, type, packets, { a, b -> a > b })

        class LessThan(version: Int, type: Int, packets: List<BITSPacket>) :
            ComparisonOperator(version, type, packets, { a, b -> a < b })

        class EqualTo(version: Int, type: Int, packets: List<BITSPacket>) :
            ComparisonOperator(version, type, packets, { a, b -> a == b })

    }

    companion object {
        fun readFrom(biterator: Biterator): BITSPacket = with(biterator) {
            val version = next(3)
            when (val type = next(3)) {
                4 -> Literal(version, type, readVarint())
                else -> when(type) {
                    0 -> Operator::Sum
                    1 -> Operator::Product
                    2 -> Operator::Min
                    3 -> Operator::Max
                    5 -> Operator::GreaterThan
                    6 -> Operator::LessThan
                    7 -> Operator::EqualTo
                    else -> error("unknown operator $type")
                }(version, type, when (next()) {
                    true -> readPackets().take(next(11)).toList()
                    false -> limit(next(15)).readPackets().toList()
                })
            }
        }
    }
}

private fun BITSPacket.walk(): Sequence<BITSPacket> = sequence {
    yield(this@walk)
    when (this@walk) {
        is BITSPacket.Literal -> Unit
        is BITSPacket.Operator -> for (packet in packets) yieldAll(packet.walk())
    }
}

private fun String.biterator() = object : Biterator {
    var bitstring = this@biterator.asSequence().joinToString("") {
        it.digitToInt(16).toString(2).padStart(4, '0')
    }

    override val remaining get() = bitstring.length

    override fun next(bits: Int) = (0 until bits).let { range ->
        bitstring.substring(range).toInt(2)
            .also { bitstring = bitstring.removeRange(range) }
    }
}
