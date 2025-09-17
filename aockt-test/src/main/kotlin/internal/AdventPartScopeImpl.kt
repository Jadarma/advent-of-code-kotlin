package io.github.jadarma.aockt.test.internal

import io.github.jadarma.aockt.test.AdventPartScope

/** A simple part scope implementation that builds a list of example inputs. */
internal class AdventPartScopeImpl : AdventPartScope {

    private val examples =  mutableListOf<Pair<PuzzleInput, PuzzleAnswer>>()

    override infix fun String.shouldOutput(expected: Any) {
        examples.add(PuzzleInput(this) to PuzzleAnswer(expected.toString()))
    }

    override infix fun Iterable<String>.shouldAllOutput(expected: Any) {
        forEach { it shouldOutput expected }
    }

    /** Applies the [block] function for each registered example. */
    inline fun forEachIndexed(block: (Int, PuzzleInput, PuzzleAnswer) -> Unit): Unit =
        examples.forEachIndexed { index, example -> block(index, example.first, example.second) }
}
