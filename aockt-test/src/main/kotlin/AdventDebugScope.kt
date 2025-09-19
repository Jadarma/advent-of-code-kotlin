package io.github.jadarma.aockt.test

import io.github.jadarma.aockt.core.Solution
import io.github.jadarma.aockt.test.internal.AocktDsl

/** A DSL scope for defining an isolated run for debugging. */
@AocktDsl
public interface AdventDebugScope {

    /** The instance of the solution being tested. */
    public val solution: Solution

    /**
     * The actual puzzle input for this puzzle.
     * Reading this property while the input is not available results in an error.
     */
    public val input: String
}
