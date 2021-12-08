import io.kotest.assertions.withClue
import io.kotest.matchers.shouldBe

fun Puzzle.testContext(vararg inputMappings: Pair<String, String> = arrayOf("input.txt" to "example.txt")) =
    SolutionCtx(
        this,
        object : InputCtx {
            val remapped = inputMappings.toMap()
            override fun SolutionCtx.resolveInput(name: String): String = with(InputCtx) {
                resolveInput(remapped[name] ?: name)
            }
        }
    )

fun Puzzle.solutionsShouldReturn(
    vararg expectedResults: Pair<String, Any>,
    context: Puzzle.() -> SolutionCtx = { testContext() },
) = with(context()) {
    expectedResults.forEach { (solution, expected) ->
        withClue(solution) { puzzle[solution]() shouldBe expected }
    }
}

fun Puzzle.solutionsShouldReturn(part1: Any = Unit, part2: Any = Unit) =
    solutionsShouldReturn("part1" to part1, "part2" to part2)
