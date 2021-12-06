import kotlin.system.measureTimeMillis

interface InputCtx {
    fun SolutionCtx.resolveInput(name: String): String

    fun <R> SolutionCtx.withInput(name: String = "input.txt", block: (Sequence<String>) -> R) =
        util.withInput(resolveInput(name), block)

    fun SolutionCtx.getInput(name: String = "input.txt") = withInput { it.toList() }

    companion object Default : InputCtx {
        override fun SolutionCtx.resolveInput(name: String) =
            "year-${puzzle.year}/day-${puzzle.day.toString().padStart(2, '0')}/$name"
    }
}

class SolutionCtx(val puzzle: Puzzle, private val inputCtx: InputCtx = InputCtx.Default) : InputCtx by inputCtx {
    val input by lazy { getInput() }
}

typealias Solution = SolutionCtx.() -> Any

open class Puzzle(val year: Int, val day: Int, val name: String? = null, solutionDefinition: PuzzleCtx.() -> Unit) {
    val solutions = buildMap {
        PuzzleCtx(this::put).solutionDefinition()
    }

    fun interface PuzzleCtx {
        fun solution(name: String, solution: Solution)
    }

    operator fun get(name: String): Solution = (solutions[name] ?: error("no solution defined for [$name]"))

    companion object {
        fun main(puzzle: Puzzle) = with(puzzle) {
            println("Year $year, Day $day${name?.let { " [$it]"} ?: ""}")
            with(SolutionCtx(puzzle)) {
                solutions.forEach { (name, solution) ->
                    lateinit var result: Any
                    val duration = measureTimeMillis {
                        result = try {
                            solution()
                        } catch (ex: NotImplementedError) {
                            "unsolved"
                        }
                    }
                    println("\t$name (took ${duration}ms): $result")
                }
            }
        }

        inline fun <reified T : Puzzle> main() {
            T::class.sealedSubclasses
                .mapNotNull { it.objectInstance }
                .forEach(::main)
        }
    }
}
