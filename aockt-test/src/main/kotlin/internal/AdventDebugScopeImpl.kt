package io.github.jadarma.aockt.test.internal

import io.github.jadarma.aockt.core.Solution
import io.github.jadarma.aockt.test.AdventDebugScope

internal class AdventDebugScopeImpl(
    override val solution: Solution,
    private val puzzleInput: PuzzleInput?,
) : AdventDebugScope {

    override val input: String
        get() = (puzzleInput ?: throw MissingInputException()).toString()
}
