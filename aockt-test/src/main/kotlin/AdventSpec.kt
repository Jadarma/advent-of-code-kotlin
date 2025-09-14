package io.github.jadarma.aockt.test

import io.github.jadarma.aockt.core.Solution
import io.github.jadarma.aockt.test.internal.AdventDayPart
import io.github.jadarma.aockt.test.internal.AdventDayPart.One
import io.github.jadarma.aockt.test.internal.AdventDayPart.Two
import io.github.jadarma.aockt.test.internal.AocktDsl
import io.github.jadarma.aockt.test.internal.MissingAdventDayAnnotationException
import io.github.jadarma.aockt.test.internal.PuzzleTestData
import io.github.jadarma.aockt.test.internal.TestData
import io.github.jadarma.aockt.test.internal.definePart
import io.github.jadarma.aockt.test.internal.id
import io.github.jadarma.aockt.test.internal.injectSolution
import io.kotest.common.ExperimentalKotest
import io.kotest.common.reflection.annotation
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.FunSpec
import io.kotest.core.test.TestCaseOrder
import io.kotest.engine.concurrency.TestExecutionMode
import io.kotest.engine.coroutines.CoroutineDispatcherFactory
import io.kotest.engine.coroutines.ThreadPerSpecCoroutineContextFactory
import kotlin.time.Duration

/**
 * A [FunSpec] specialized for testing Advent of Code puzzle [Solution]s.
 * The test classes extending this should also provide information about the puzzle with an [AdventDay] annotation.
 *
 * Example:
 * ```kotlin
 * import io.github.jadarma.aockt.core.Solution
 * import io.github.jadarma.aockt.test.AdventSpec
 * import io.github.jadarma.aockt.test.AdventDay
 *
 * @AdventDay(2015, 1, "Not Quite Lisp")
 * class Y2015D01Test : AdventSpec<Y2015D01>({
 *     partOne {
 *         listOf("(())", "()()") shouldAllOutput 0
 *         listOf("(((", "(()(()(") shouldAllOutput 3
 *         listOf("())", "))(") shouldAllOutput -1
 *         listOf(")))", ")())())") shouldAllOutput -3
 *     }
 *     partTwo {
 *         ")" shouldOutput 1
 *         "()())" shouldOutput 5
 *     }
 * }
 * ```
 *
 * @param T    The implementation class of the [Solution] to be tested.
 * @param body A context in which to configure the tests.
 */
@Suppress("UnnecessaryAbstractClass")
@OptIn(ExperimentalKotest::class)
@AocktDsl
public abstract class AdventSpec<T : Solution>(
    body: AdventSpec<T>.() -> Unit = {},
) : FunSpec() {

    internal val testData: PuzzleTestData
    internal val definedParts: MutableSet<AdventDayPart> = mutableSetOf()

    /** The instance of the solution to be tested. */
    @Suppress("MemberVisibilityCanBePrivate")
    public val solution: Solution

    init {
        val adventDay = this::class.annotation<AdventDay>() ?: throw MissingAdventDayAnnotationException(this::class)
        solution = injectSolution()
        testData = TestData.inputFor(adventDay.id)
        body()
    }

    // Enforce some configuration to ensure that all tests within one AdventSpec will be executed sequentially on a
    // single thread.
    final override fun coroutineDispatcherFactory(): CoroutineDispatcherFactory = ThreadPerSpecCoroutineContextFactory
    final override fun isolationMode(): IsolationMode = IsolationMode.SingleInstance
    final override fun testCaseOrder(): TestCaseOrder = TestCaseOrder.Sequential
    final override fun testExecutionMode(): TestExecutionMode = TestExecutionMode.Sequential

    /**
     * Provides a context to test the implementation a [Solution.partOne] function.
     * Should be called at most once per [AdventSpec].
     *
     * Will create a context with two tests:
     *  - Verifies the output, given the input file has been added to the test resources.
     *    If the solution is known as well, also validates the answer matches it.
     *  - Verifies the given examples in an [AdventPartScope], useful for a TDD approach when
     *    implementing the solution for the first time.
     *
     * @param enabled             If set to false, part one will not be tested.
     * @param expensive           This part is known to produce answers in a longer timespan.
     * @param executionMode       Specifies which tests defined for this part will be enabled.
     * @param efficiencyBenchmark The maximum amount of time a solution can take to finish to be considered efficient.
     * @param test                Test the solution against example inputs defined in this [AdventPartScope].
     */
    public fun partOne(
        enabled: Boolean = true,
        expensive: Boolean = false,
        executionMode: ExecMode? = null,
        efficiencyBenchmark: Duration? = null,
        test: (AdventPartScope.() -> Unit)? = null,
    ): Unit = definePart(One, enabled, expensive, executionMode, efficiencyBenchmark, test)

    /**
     * Provides a context to test the implementation a [Solution.partTwo] function.
     * Should be called at most once per [AdventSpec].
     *
     * Will create a context with two tests:
     *  - Verifies the output, given the input file has been added to the test resources.
     *    If the solution is known as well, also validates the answer matches it.
     *  - Verifies the given examples in an [AdventPartScope], useful for a TDD approach when
     *    implementing the solution for the first time.
     *
     * @param enabled             If set to false, part one will not be tested.
     * @param expensive           This part is known to produce answers in a longer timespan.
     * @param executionMode       Specifies which tests defined for this part will be enabled.
     * @param efficiencyBenchmark The maximum amount of time a solution can take to finish to be considered efficient.
     * @param test                Test the solution against example inputs defined in this [AdventPartScope].
     */
    public fun partTwo(
        enabled: Boolean = true,
        expensive: Boolean = false,
        executionMode: ExecMode? = null,
        efficiencyBenchmark: Duration? = null,
        test: (AdventPartScope.() -> Unit)? = null,
    ): Unit = definePart(Two, enabled, expensive, executionMode, efficiencyBenchmark, test)
}
