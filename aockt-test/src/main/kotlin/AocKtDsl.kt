// Copyright © 2020 Dan Cîmpianu
// This code is licensed under the MIT license, detailed in LICENSE.md or at https://opensource.org/license/MIT.
package io.github.jadarma.aockt

import io.github.jadarma.aockt.internal.AocKtDsl
import kotlin.time.Duration

/** A DSL scope for enabling and tweaking configs of part tests. */
@AocKtDsl
public interface AdventRootScope {

    /**
     * Provides a context to test the implementation a [Solution.partOne] function.
     * Should be called at most once per scope.
     *
     * @param enabled             If set to false, the root context will be registered but not executed.
     * @param executionMode       Specifies which tests defined for this part will be enabled as an optional override
     *                            for this test only.
     *                            If not provided, the project-default will be used.
     * @param efficiencyBenchmark Specifies the maximum amount of time the solution can take to finish to be considered
     *                            efficient as an optional override for this test only.
     *                            If not provided, the project-default will be used.
     * @param expensive           Whether this part is known to produce answers in a longer timespan.
     *                            If enabled, the tests will be tagged as such, and efficiency benchmark tests will be
     *                            skipped.
     * @param examples            Test the solution against example inputs defined in this [AdventPartScope].
     */
    public fun partOne(
        enabled: Boolean = true,
        executionMode: ExecMode? = null,
        efficiencyBenchmark: Duration? = null,
        expensive: Boolean = false,
        examples: AdventPartScope.() -> Unit = {},
    )

    /**
     * Provides a context to test the implementation a [Solution.partTwo] function.
     * Should be called at most once per scope.
     *
     * @param enabled             If set to false, the root context will be registered but not executed.
     * @param executionMode       Specifies which tests defined for this part will be enabled as an optional override
     *                            for this test only.
     *                            If not provided, the project-default will be used.
     * @param efficiencyBenchmark Specifies the maximum amount of time the solution can take to finish to be considered
     *                            efficient as an optional override for this test only.
     *                            If not provided, the project-default will be used.
     * @param expensive           Whether this part is known to produce answers in a longer timespan.
     *                            If enabled, the tests will be tagged as such, and efficiency benchmark tests will be
     *                            skipped.
     * @param examples            Test the solution against example inputs defined in this [AdventPartScope].
     */
    public fun partTwo(
        enabled: Boolean = true,
        executionMode: ExecMode? = null,
        efficiencyBenchmark: Duration? = null,
        expensive: Boolean = false,
        examples: AdventPartScope.() -> Unit = {},
    )

    /**
     * If used, ignores all other tests, and only runs this [test] lambda.
     * The solution instance being tested is exposed in the scope.
     * Useful when running the test in debug mode, to ensure only this test will trigger any breakpoints.
     *
     * @param test A context to execute a debug run in.
     */
    public fun debug(test: AdventDebugScope.() -> Unit)
}

/** A DSL scope for defining example assertions for puzzle parts. */
@AocKtDsl
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

/** A DSL scope for defining an isolated run for debugging. */
@AocKtDsl
public interface AdventDebugScope {

    /** The instance of the solution being tested. */
    public val solution: Solution

    /**
     * The actual puzzle input for this puzzle.
     * Reading this property while the input is not available results in an error.
     */
    public val input: String
}

/** Configures which inputs the tests will run on. */
public enum class ExecMode {
    /** Run both tests and the user input, if available. */
    All,

    /** Do not run the user input, even if available. */
    ExamplesOnly,

    /** Do not run the defined examples. */
    SkipExamples;
}
