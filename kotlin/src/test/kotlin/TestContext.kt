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
