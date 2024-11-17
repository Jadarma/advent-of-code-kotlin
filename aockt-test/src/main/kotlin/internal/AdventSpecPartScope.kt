package io.github.jadarma.aockt.test.internal

import io.github.jadarma.aockt.test.AdventSpec

/** A simple part scope implementation that builds a list of example inputs. */
internal class AdventSpecPartScope : AdventSpec.PartScope {

    private val examples =  mutableListOf<Pair<PuzzleInput, String>>()

    override infix fun String.shouldOutput(expected: Any) {
        examples.add(PuzzleInput(this) to expected.toString())
    }

    override infix fun Iterable<String>.shouldAllOutput(expected: Any) {
        forEach { it shouldOutput expected }
    }

    /** Applies the [block] function for each registered example. */
    inline fun forEachIndexed(block: (Int, PuzzleInput, String) -> Unit) =
        examples.forEachIndexed { index, example -> block(index, example.first, example.second) }
}
