package year2021

import Puzzle

fun main() = Puzzle.main(Day16)

object Day16 : Puzzle2021(16, {
    part1 {
        input.map { line ->
            println(line)
            BITSPacket.readFrom(line.biterator()).walk().sumOf { it.version }
        }
    }

    part2 {
        input.map { line ->
            BITSPacket.readFrom(line.biterator()).also { println(it.render()) }.value
        }
    }
})

private interface Biterator : Iterator<Boolean> {
    fun next(bits: Int): Int
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

    override fun next(bits: Int): Int {
        require(consumed + bits <= n)
        return this@limit.next(bits).also { consumed += bits }
    }

    override fun hasNext() = consumed < n
}

private fun Biterator.readPackets() = sequence { while (hasNext()) yield(readPacket()) }
private fun Biterator.readPacket() = BITSPacket.readFrom(this)

private sealed interface BITSPacket {
    val version: Int
    val value: Long

    data class Literal(override val version: Int, override val value: Long) : BITSPacket
    sealed class Operator(override val version: Int, val packets: List<BITSPacket>) : BITSPacket {
        sealed class ReducingOperator(version: Int, packets: List<BITSPacket>, reducer: (Long, Long) -> Long) :
            Operator(version, packets) {
            override val value = packets.takeUnless { it.isEmpty() }?.map { it.value }?.reduce(reducer) ?: error("wat? $this")
        }

        sealed class BinaryOperator(version: Int, packets: List<BITSPacket>, operator: (Long, Long) -> Boolean) :
            Operator(version, packets) {
            override val value = packets.let { (a, b) -> if (operator(a.value, b.value)) 1L else 0L }
        }

        class Sum(version: Int, packets: List<BITSPacket>) : ReducingOperator(version, packets, Long::plus)
        class Product(version: Int, packets: List<BITSPacket>) : ReducingOperator(version, packets, Long::times)
        class Min(version: Int, packets: List<BITSPacket>) : ReducingOperator(version, packets, ::minOf)
        class Max(version: Int, packets: List<BITSPacket>) : ReducingOperator(version, packets, ::maxOf)
        class GreaterThan(version: Int, packets: List<BITSPacket>) : BinaryOperator(version, packets, { a, b -> a > b })
        class LessThan(version: Int, packets: List<BITSPacket>) : BinaryOperator(version, packets, { a, b -> a < b })
        class EqualTo(version: Int, packets: List<BITSPacket>) : BinaryOperator(version, packets, { a, b -> a == b })
    }

    companion object {
        fun readFrom(biterator: Biterator): BITSPacket = with(biterator) {
            val version = next(3)
            when (val type = next(3)) {
                4 -> Literal(version, readVarint())
                else -> when (type) {
                    0 -> Operator::Sum
                    1 -> Operator::Product
                    2 -> Operator::Min
                    3 -> Operator::Max
                    5 -> Operator::GreaterThan
                    6 -> Operator::LessThan
                    7 -> Operator::EqualTo
                    else -> error("unknown operator $type")
                }.invoke(
                    version, when (next()) {
                        true -> readPackets().take(next(11)).toList()
                        false -> limit(next(15)).readPackets().toList()
                    }
                )
            }
        }
    }
}

private fun BITSPacket.Operator.render(sep: String = ", ", prefix: String = "") =
    packets.joinToString(sep, "$prefix(", postfix = ")", transform = BITSPacket::render)

private fun BITSPacket.render(): String = when (this@render) {
    is BITSPacket.Literal -> value.toString()
    is BITSPacket.Operator.Sum -> render(sep = " + ")
    is BITSPacket.Operator.Product -> render(sep = " * ")
    is BITSPacket.Operator.Min -> render(prefix = "min")
    is BITSPacket.Operator.Max -> render(prefix = "max")
    is BITSPacket.Operator.GreaterThan -> render(sep = " > ")
    is BITSPacket.Operator.LessThan -> render(sep = " < ")
    is BITSPacket.Operator.EqualTo -> render(sep = " = ")
}

private fun BITSPacket.walk(): Sequence<BITSPacket> = sequence {
    yield(this@walk)
    when (this@walk) {
        is BITSPacket.Literal -> Unit
        is BITSPacket.Operator -> for (packet in packets) yieldAll(packet.walk())
    }
}

private fun String.decodeHex() = ByteArray(length / 2) {
    substring(it * 2, (it + 1) * 2).toInt(16).toByte()
}

private fun String.biterator() = decodeHex().biterator()

private fun ByteArray.biterator() = object : Biterator {
    val bytes = this@biterator.iterator()
    var current: Int = 0
    var availableBits = 0

    private fun prepareNext() {
        current = bytes.nextByte().toInt() and 0xFF
        availableBits = 8
    }

    override fun next(bits: Int): Int {
        if (availableBits == 0) prepareNext()
        return when {
            bits == 8 && availableBits == 8 -> current.also { availableBits = 0 }
            bits <= availableBits -> {
                availableBits -= bits
                current shr availableBits and masks[bits]
            }
            bits <= 31 -> {
                val rem = bits - availableBits
                var acc = next(availableBits)
                repeat(rem / 8) { acc = acc shl 8 or next(8) }
                when (val last = (rem % 8)) {
                    0 -> acc
                    else -> acc shl last or next(last)
                }
            }
            else -> error("reading more than 31 bits at a time is not supported")
        }
    }

    private val masks = intArrayOf(0b0, 0b1, 0b11, 0b111, 0b1111, 0b1_1111, 0b11_1111, 0b111_1111)

    override fun hasNext() = bytes.hasNext() || availableBits > 0
}
