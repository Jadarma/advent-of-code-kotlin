@file:OptIn(ExperimentalKotest::class)

package io.github.jadarma.aockt.test

import io.github.jadarma.aockt.core.Solution
import io.github.jadarma.aockt.test.internal.AdventDayID
import io.github.jadarma.aockt.test.internal.AdventDayPart
import io.github.jadarma.aockt.test.internal.AdventDayPart.One
import io.github.jadarma.aockt.test.internal.AdventDayPart.Two
import io.github.jadarma.aockt.test.internal.ConflictingPartExampleConfigurationException
import io.github.jadarma.aockt.test.internal.MissingAdventDayAnnotationException
import io.github.jadarma.aockt.test.internal.PuzzleTestData
import io.github.jadarma.aockt.test.internal.TestData
import io.github.jadarma.aockt.test.internal.id
import io.github.jadarma.aockt.test.internal.partFunction
import io.kotest.common.ExperimentalKotest
import io.kotest.core.spec.style.FunSpec
import io.kotest.mpp.annotation

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
 * class Y2015D01Test : AdventSpec(2015D01(), {
 *     // ...
 * }
 * ```
 */
public abstract class AdventSpec(
    protected val solution: Solution,
    body: AdventSpec.() -> Unit = {},
) : FunSpec() {

    private val adventDayId: AdventDayID
    private val testData: PuzzleTestData

    init {
        val adventDay = this::class.annotation<AdventDay>() ?: throw MissingAdventDayAnnotationException(this::class)
        adventDayId = adventDay.id
        testData = TestData.inputFor(adventDayId)
        body()
    }

    /**
     * Provides a context to test the implementation of one of a [Solution]'s part function.
     *
     * Will create a context with two tests:
     *  - Verifies the output, given the input file has been added to the test resources.
     *    If the solution is known as well, also validates the answer matches it.
     *  - Verifies the given examples in an [AdventPartTestContext], useful for a TDD approach when implementing the
     *    solution for the first time.
     *
     * @param enabled If set to false, part one will not be tested.
     * @param expensive This part is known to produce answers in a longer timespan.
     * @param examplesOnly Only run the examples, and don't test against actual input.
     * @param skipExamples Only run against actual input.
     * @param examples Test the solution against example inputs defined in this [AdventPartTestContext].
     */
    @Suppress("LongParameterList")
    private fun partTest(
        part: AdventDayPart,
        enabled: Boolean,
        expensive: Boolean,
        examplesOnly: Boolean,
        skipExamples: Boolean,
        examples: (suspend AdventPartTestContext.() -> Unit)?,
    ) {
        if (examplesOnly && skipExamples) {
            throw ConflictingPartExampleConfigurationException(this::class)
        }

        val partFunction = solution.partFunction(part)

        context("Part $part").config(
            enabled = enabled,
            tags = if (expensive) setOf(ExpensiveDay) else emptySet(),
        ) {
            test("Outputs a solution").config(enabled = testData.input != null) {
                // TODO: Test and display the results.
            }

            if (examples != null) {
                test("Validates the examples").config(enabled = !skipExamples) {
                    AdventPartTestContext(partFunction).examples()
                }
            }
        }
    }

    /**
     * Provides a context to test the implementation a [Solution.partOne] function.
     * Should only be called once per [AdventSpec].
     *
     * Will create a context with two tests:
     *  - Verifies the output, given the input file has been added to the test resources.
     *    If the solution is known as well, also validates the answer matches it.
     *  - Verifies the given examples in an [AdventPartTestContext], useful for a TDD approach when implementing the
     *    solution for the first time.
     *
     * @param enabled If set to false, part one will not be tested.
     * @param expensive This part is known to produce answers in a longer timespan.
     * @param examplesOnly Only run the examples, and don't test against actual input.
     * @param skipExamples Only run against actual input.
     * @param test Test the solution against example inputs defined in this [AdventPartTestContext].
     */
    public fun partOne(
        enabled: Boolean = true,
        expensive: Boolean = false,
        examplesOnly: Boolean = false,
        skipExamples: Boolean = false,
        test: (suspend AdventPartTestContext.() -> Unit)? = null,
    ): Unit = partTest(
        part = One,
        enabled = enabled,
        expensive = expensive,
        examplesOnly = examplesOnly,
        skipExamples = skipExamples,
        examples = test,
    )

    /**
     * Provides a context to test the implementation a [Solution.partTwo] function.
     * Should only be called once per [AdventSpec].
     *
     * Will create a context with two tests:
     *  - Verifies the output, given the input file has been added to the test resources.
     *    If the solution is known as well, also validates the answer matches it.
     *  - Verifies the given examples in an [AdventPartTestContext], useful for a TDD approach when implementing the
     *    solution for the first time.
     *
     * @param enabled If set to false, part one will not be tested.
     * @param expensive This part is known to produce answers in a longer timespan.
     * @param examplesOnly Only run the examples, and don't test against actual input.
     * @param skipExamples Only run against actual input.
     * @param test Test the solution against example inputs defined in this [AdventPartTestContext].
     */
    public fun partTwo(
        enabled: Boolean = true,
        expensive: Boolean = false,
        examplesOnly: Boolean = false,
        skipExamples: Boolean = false,
        test: (suspend AdventPartTestContext.() -> Unit)? = null,
    ): Unit = partTest(
        part = Two,
        enabled = enabled,
        expensive = expensive,
        examplesOnly = examplesOnly,
        skipExamples = skipExamples,
        examples = test,
    )
}
