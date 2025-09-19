package io.github.jadarma.aockt.test

import io.github.jadarma.aockt.core.Solution
import io.github.jadarma.aockt.test.internal.AocktDsl
import kotlin.time.Duration

/** A DSL scope for enabling and tweaking configs of part tests. */
@AocktDsl
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
     */
    public fun debug(test: AdventDebugScope.() -> Unit)
}
