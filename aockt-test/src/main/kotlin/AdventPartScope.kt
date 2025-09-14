package io.github.jadarma.aockt.test

import io.github.jadarma.aockt.test.internal.AocktDsl

/** A DSL scope for defining example assertions for puzzle parts. */
@AocktDsl
public interface AdventPartScope {

    /**
     * Creates a new test that asserts that when given this string as input, it gets the correct [expected] answer.
     * The [expected] value can be anything and will be tested against its `.toString()` value.
     *
     * @receiver The example puzzle input.
     * @param expected The correct answer to the puzzle for the given input.
     */
    public infix fun String.shouldOutput(expected: Any)

    /**
     * For each of the values given creates a new test that asserts that when given as input, it gets the correct
     * [expected] answer.
     * The [expected] value can be anything and will be tested against its `.toString()` value.
     *
     * _NOTE:_ This should be equivalent to calling [shouldOutput] for every input.
     *
     * @receiver The example puzzle inputs.
     * @param expected The correct answer to the puzzle for all given inputs.
     */
    public infix fun Iterable<String>.shouldAllOutput(expected: Any)
}
