import kotlin.system.measureTimeMillis

interface InputCtx {
    fun SolutionCtx.resolveInput(name: String): String

    fun <R> SolutionCtx.withInput(name: String = "input.txt", block: (Sequence<String>) -> R) =
        util.withInput(resolveInput(name), block)

    fun SolutionCtx.getInput(name: String = "input.txt") = withInput(name) { it.toList() }

    companion object Default : InputCtx {
        override fun SolutionCtx.resolveInput(name: String) =
            "year-${puzzle.year}/day-${puzzle.day.toString().padStart(2, '0')}/$name"
    }
}

class SolutionCtx(val puzzle: Puzzle, private val inputCtx: InputCtx = InputCtx.Default) : InputCtx by inputCtx {
    val input by lazy { getInput() }
}

typealias Solution = SolutionCtx.() -> Any

open class Puzzle(val year: Int, val day: Int, solutionDefinition: PuzzleCtx.() -> Unit) {
    val solutions = buildMap {
        PuzzleCtx(this::put).solutionDefinition()
    }

    fun interface PuzzleCtx {
        fun solution(name: String, solution: Solution)
        fun part1(solution: Solution) = solution("part1", solution)
        fun part2(solution: Solution) = solution("part2", solution)
        operator fun String.invoke(solution: Solution) = solution(this, solution)
    }

    operator fun get(name: String): Solution = (solutions[name] ?: error("no solution defined for [$name]"))

    companion object {
        fun main(puzzle: Puzzle) = with(puzzle) {
            println("Year $year, Day $day (${this::class.qualifiedName ?: "anon"})")
            with(SolutionCtx(puzzle)) {
                solutions.forEach { (name, solution) ->
                    lateinit var result: Any
                    val duration = measureTimeMillis {
                        result = try {
                            solution().takeIf { it != Unit } ?: "unsolved"
                        } catch (ex: NotImplementedError) {
                            "unsolved"
                        }
                    }
                    result = result.let { if (it is String && '\n' in it) "\n$it" else it }
                    println("\t$name (took ${duration}ms): $result")
                }
            }
        }

        inline fun <reified T: Puzzle> all() = T::class.sealedSubclasses.asSequence().mapNotNull { it.objectInstance }

        inline fun <reified T : Puzzle> main() {
            all<T>().forEach(::main)
        }
    }
}
