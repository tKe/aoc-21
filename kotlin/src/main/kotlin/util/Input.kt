package util

import java.nio.file.Files
import java.nio.file.Paths

private fun findDirectoryInAncestors(name: String) = generateSequence(Paths.get(name).toAbsolutePath()) {
    it.parent?.parent?.resolve(name)
}.firstOrNull { Files.isDirectory(it) }

private val basePath by lazy {
    findDirectoryInAncestors("inputs")
        ?: findDirectoryInAncestors("aoc-21-inputs")
        ?: error("unable to find inputs directory")
}

fun <R> withInput(path: String, block: (Sequence<String>) -> R): R =
    basePath.resolve(path).toFile().useLines(block = block)
