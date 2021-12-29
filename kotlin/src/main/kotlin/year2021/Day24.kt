package year2021

import Puzzle
import SolutionCtx

fun main() = Puzzle.main(Day24)

object Day24 : Puzzle2021(24) {
    val part1 by solution {
        parseProgram().solve(9 downTo 1) { z == 0 }

    }

    val part2 by solution {
        parseProgram().solve(1..9) { z == 0 }
    }

    private fun ALUExpr.requiredRegisters(): Sequence<Char> = when (this) {
        is ALUExpr.BinaryOp -> sequenceOf(a, b).flatMap { it.requiredRegisters() }.distinct()
        is ALUExpr.Register -> sequenceOf(register)
        else -> emptySequence()
    }

    private fun Step.solve(
        inputProgression: IntProgression,
        registers: ALURegisters = ALURegisters(),
        cache: MutableMap<Any, Long> = mutableMapOf(),
        predicate: ALURegisters.() -> Boolean,
    ): Long = cache.getOrPut(id to registers) {
        inputProgression.firstNotNullOfOrNull { digit ->
            when (next) {
                null -> digit.takeIf { predicate(invoke(digit, registers)) }?.toLong()
                else -> {
                    val nextRegs = invoke(digit, registers, next.requiredRegisters)
                    next.solve(inputProgression, nextRegs, cache, predicate).takeIf { it != -1L }
                        ?.let { "$digit$it".toLong() }
                }
            }
        } ?: -1L
    }


    private data class Step(val id: Int, val expressions: Map<Char, ALUExpr>, val next: Step?) {
        val requiredRegisters by lazy { expressions.values.flatMap { it.requiredRegisters() }.toSet() }
        operator fun invoke(input: Int, inputRegisters: ALURegisters, outputRegisters: Set<Char> = "wxyz".toSet()) =
            ALURegisters(
                w = if ('w' in outputRegisters) expressions.getValue('w').evaluate(input, inputRegisters) else 0,
                x = if ('x' in outputRegisters) expressions.getValue('x').evaluate(input, inputRegisters) else 0,
                y = if ('y' in outputRegisters) expressions.getValue('y').evaluate(input, inputRegisters) else 0,
                z = if ('z' in outputRegisters) expressions.getValue('z').evaluate(input, inputRegisters) else 0,
            )
    }

    private data class RawOp(val op: String, val register: Char, val v: String?)

    private fun SolutionCtx.parseProgram(): Step {
        val steps = input.run {
            val inputInstructions = asSequence()
                .mapIndexedNotNull { index, s -> index.takeIf { s.startsWith("inp") } } + size
            inputInstructions.zipWithNext { a, b ->
                subList(a, b).map { line ->
                    line.split(' ').let { RawOp(it[0], it[1][0], it.getOrNull(2)) }
                }
            }.toList()
        }

        return steps.foldRightIndexed(null as Step?) { stepIdx, step, next -> Step(stepIdx, step.parse(), next) }
            ?: error("empty program")
    }

    private fun List<RawOp>.parse(cache: MutableMap<Int, ALUExpr> = mutableMapOf()) =
        buildMap {
            forEachIndexed { index, (op, r) ->
                cache.getOrPut(index) {
                    when (op) {
                        "inp" -> ALUExpr.Input
                        else -> {
                            parseExpr(this@parse[index]) {
                                val prevMutation =
                                    (0 until index).reversed().firstOrNull { idx -> this@parse[idx].register == it }
                                        ?: -1
                                cache[prevMutation] ?: ALUExpr.Register(it)
                            }
                        }
                    }
                }.also { put(r, it) }
            }
        }

    private fun parseExpr(opCall: RawOp, resolver: (Char) -> ALUExpr): ALUExpr {
        val (op, r, v) = opCall
        return if (op == "mul" && v == "0") ALUExpr.Zero
        else {
            val a = resolver(r)
            val b = v!!.toIntOrNull()?.let { ALUExpr.Constant(it) } ?: resolver(v[0])
            when (op) {
                "mul" -> when {
                    a == ALUExpr.Zero || ALUExpr.Zero == b -> ALUExpr.Zero
                    a is ALUExpr.Constant && b is ALUExpr.Constant -> ALUExpr.Constant(a.value * b.value)
                    else -> ALUExpr.BinaryOp.Mul(a, b)
                }
                "add" -> when {
                    ALUExpr.Zero == a && ALUExpr.Zero == b -> ALUExpr.Zero
                    ALUExpr.Zero == a -> b
                    ALUExpr.Zero == b -> a
                    a is ALUExpr.Constant && b is ALUExpr.Constant -> ALUExpr.Constant(a.value + b.value)
                    else -> ALUExpr.BinaryOp.Add(a, b)
                }
                "div" -> when {
                    b == ALUExpr.One -> a
                    a is ALUExpr.Constant && b is ALUExpr.Constant -> ALUExpr.Constant(a.value / b.value)
                    else -> ALUExpr.BinaryOp.Div(a, b)
                }
                "mod" -> when {
                    a == ALUExpr.Zero -> ALUExpr.Zero
                    a is ALUExpr.Constant && b is ALUExpr.Constant -> ALUExpr.Constant(a.value % b.value)
                    else -> ALUExpr.BinaryOp.Mod(a, b)
                }
                "eql" -> when {
                    a is ALUExpr.Constant && b is ALUExpr.Constant -> if (a == b) ALUExpr.One else ALUExpr.Zero
                    else -> ALUExpr.BinaryOp.Eql(a, b)
                }
                else -> error("unknown op $op")
            }
        }
    }
}

data class ALURegisters(val w: Int = 0, val x: Int = 0, val y: Int = 0, val z: Int = 0)

private sealed interface ALUExpr {
    fun evaluate(input: Int, registers: ALURegisters): Int

    object Input : ALUExpr {
        override fun evaluate(input: Int, registers: ALURegisters) = input
        override fun toString() = "Input"
    }

    sealed class BinaryOp(open val a: ALUExpr, open val b: ALUExpr) : ALUExpr {
        data class Add(override val a: ALUExpr, override val b: ALUExpr) : BinaryOp(a, b) {
            override fun evaluate(a: Int, b: Int) = a + b
        }

        data class Mul(override val a: ALUExpr, override val b: ALUExpr) : BinaryOp(a, b) {
            override fun evaluate(a: Int, b: Int) = a * b
        }

        data class Mod(override val a: ALUExpr, override val b: ALUExpr) : BinaryOp(a, b) {
            override fun evaluate(a: Int, b: Int) = a % b
        }

        data class Div(override val a: ALUExpr, override val b: ALUExpr) : BinaryOp(a, b) {
            override fun evaluate(a: Int, b: Int) = a / b
        }

        data class Eql(override val a: ALUExpr, override val b: ALUExpr) :
            BinaryOp(a, b) {
            override fun evaluate(a: Int, b: Int) = if (a == b) 1 else 0
        }

        abstract fun evaluate(a: Int, b: Int): Int
        override fun evaluate(input: Int, registers: ALURegisters) =
            evaluate(a.evaluate(input, registers), b.evaluate(input, registers))
    }

    data class Constant(val value: Int) : ALUExpr {
        override fun evaluate(input: Int, registers: ALURegisters) = value
    }

    data class Register(val register: Char) : ALUExpr {
        override fun evaluate(input: Int, registers: ALURegisters) = when (register) {
            'w' -> registers.w
            'x' -> registers.x
            'y' -> registers.y
            'z' -> registers.z
            else -> error("incorrect register $register")
        }
    }

    companion object {
        val Zero = Constant(0)
        val One = Constant(1)
    }
}
