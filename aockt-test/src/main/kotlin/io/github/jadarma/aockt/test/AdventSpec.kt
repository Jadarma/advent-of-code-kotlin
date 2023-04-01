@file:OptIn(ExperimentalKotest::class)

package io.github.jadarma.aockt.test

import io.github.jadarma.aockt.core.Solution
import io.github.jadarma.aockt.test.internal.AdventDayID
import io.github.jadarma.aockt.test.internal.AdventDayPart
import io.github.jadarma.aockt.test.internal.AdventDayPart.One
import io.github.jadarma.aockt.test.internal.AdventDayPart.Two
import io.github.jadarma.aockt.test.internal.ConflictingPartExampleConfigurationException
import io.github.jadarma.aockt.test.internal.MissingAdventDayAnnotationException
import io.github.jadarma.aockt.test.internal.MissingNoArgConstructorException
import io.github.jadarma.aockt.test.internal.PuzzleTestData
import io.github.jadarma.aockt.test.internal.TestData
import io.github.jadarma.aockt.test.internal.id
import io.github.jadarma.aockt.test.internal.partFunction
import io.kotest.common.ExperimentalKotest
import io.kotest.core.spec.style.FunSpec
import io.kotest.mpp.annotation
import io.kotest.mpp.newInstanceNoArgConstructorOrObjectInstance
import kotlin.reflect.KClass
import kotlin.reflect.full.isSubtypeOf
import kotlin.reflect.full.starProjectedType
import kotlin.reflect.jvm.jvmErasure
import kotlin.reflect.typeOf

/**
 * A [FunSpec] specialized for testing Advent of Code puzzle [Solution]s.
 * The test classes extending this should also provide information about the puzzle with an [AdventDay] annotation.
 *
 * @param T The implementation class of the [Solution] to be tested.
 *
 * Example:
 * ```kotlin
 * import io.github.jadarma.aockt.core.Solution
 * import io.github.jadarma.aockt.test.AdventSpec
 * import io.github.jadarma.aockt.test.AdventDay
 *
 * @AdventDay(2015, 1, "Not Quite Lisp")
 * class Y2015D01Test : AdventSpec<Y2015D01>({
 *     // ...
 * }
 * ```
 */
public abstract class AdventSpec<T : Solution>(
    body: AdventSpec<T>.() -> Unit = {},
) : FunSpec() {

    private val adventDayId: AdventDayID
    private val testData: PuzzleTestData

    /**
     * The implementation class of the [Solution] to be tested.
     * Injected by some reflection magic that while not that pretty, is fine for use in unit tests, and allows for a
     * more elegant syntax when declaring [AdventSpec]s.
     */
    private val solution: Solution = this::class
        .starProjectedType.jvmErasure.supertypes
        .first { it.isSubtypeOf(typeOf<AdventSpec<*>>()) }
        .arguments.first().type!!.jvmErasure
        .run {
            @Suppress("UNCHECKED_CAST") // Must be a solution because of AdventSpec bounds.
            this as KClass<Solution>

            runCatching { newInstanceNoArgConstructorOrObjectInstance() }
                .getOrElse { throw MissingNoArgConstructorException(this) }
        }

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
     *  - Verifies the given examples in an [AdventSpecExampleContainerScope], useful for a TDD approach when
     *    implementing the solution for the first time.
     *
     * @param enabled If set to false, part one will not be tested.
     * @param expensive This part is known to produce answers in a longer timespan.
     * @param examplesOnly Only run the examples, and don't test against actual input.
     * @param skipExamples Only run against actual input.
     * @param examples Test the solution against example inputs defined in this [AdventSpecExampleContainerScope].
     */
    @Suppress("LongParameterList")
    private fun partTest(
        part: AdventDayPart,
        enabled: Boolean,
        expensive: Boolean,
        examplesOnly: Boolean,
        skipExamples: Boolean,
        examples: (suspend AdventSpecExampleContainerScope.() -> Unit)?,
    ) {
        if (examplesOnly && skipExamples) {
            throw ConflictingPartExampleConfigurationException(this::class)
        }

        val partFunction = solution.partFunction(part)

        context("Part $part").config(
            enabled = enabled,
            tags = if (expensive) setOf(ExpensiveDay) else emptySet(),
        ) {
            test("Outputs a solution").config(enabled = testData.input != null && !examplesOnly) {
                // TODO: Test and display the results.
            }

            if (examples != null) {
                context("Validates the examples").config(enabled = !skipExamples) {
                    AdventSpecExampleContainerScope(partFunction, this).examples()
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
     *  - Verifies the given examples in an [AdventSpecExampleContainerScope], useful for a TDD approach when
     *    implementing the solution for the first time.
     *
     * @param enabled If set to false, part one will not be tested.
     * @param expensive This part is known to produce answers in a longer timespan.
     * @param examplesOnly Only run the examples, and don't test against actual input.
     * @param skipExamples Only run against actual input.
     * @param test Test the solution against example inputs defined in this [AdventSpecExampleContainerScope].
     */
    public fun partOne(
        enabled: Boolean = true,
        expensive: Boolean = false,
        examplesOnly: Boolean = false,
        skipExamples: Boolean = false,
        test: (suspend AdventSpecExampleContainerScope.() -> Unit)? = null,
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
     *  - Verifies the given examples in an [AdventSpecExampleContainerScope], useful for a TDD approach when
     *    implementing the solution for the first time.
     *
     * @param enabled If set to false, part one will not be tested.
     * @param expensive This part is known to produce answers in a longer timespan.
     * @param examplesOnly Only run the examples, and don't test against actual input.
     * @param skipExamples Only run against actual input.
     * @param test Test the solution against example inputs defined in this [AdventSpecExampleContainerScope].
     */
    public fun partTwo(
        enabled: Boolean = true,
        expensive: Boolean = false,
        examplesOnly: Boolean = false,
        skipExamples: Boolean = false,
        test: (suspend AdventSpecExampleContainerScope.() -> Unit)? = null,
    ): Unit = partTest(
        part = Two,
        enabled = enabled,
        expensive = expensive,
        examplesOnly = examplesOnly,
        skipExamples = skipExamples,
        examples = test,
    )
}
