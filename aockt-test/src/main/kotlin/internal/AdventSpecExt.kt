package io.github.jadarma.aockt.test.internal

import io.github.jadarma.aockt.core.Solution
import io.github.jadarma.aockt.test.AdventPartScope
import io.github.jadarma.aockt.test.AdventSpec
import io.github.jadarma.aockt.test.ExecMode
import io.github.jadarma.aockt.test.Expensive
import io.kotest.assertions.AssertionErrorBuilder
import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.withClue
import io.kotest.common.ExperimentalKotest
import io.kotest.common.reflection.ReflectionInstantiations.newInstanceNoArgConstructorOrObjectInstance
import io.kotest.core.spec.style.scopes.FunSpecContainerScope
import io.kotest.matchers.comparables.shouldBeLessThanOrEqualTo
import io.kotest.matchers.shouldBe
import kotlin.reflect.KClass
import kotlin.reflect.full.isSubtypeOf
import kotlin.reflect.full.starProjectedType
import kotlin.reflect.jvm.jvmErasure
import kotlin.reflect.typeOf
import kotlin.time.Duration
import kotlin.time.measureTimedValue

/**
 * Construct and inject a solution instance for this [AdventSpec].
 * This function is designed to be called at spec instantiation time.
 * Uses reflection magic that while not that pretty, is fine for use in unit tests, and allows for a more elegant syntax
 * when declaring specs.
 */
@Suppress("UNCHECKED_CAST", "UnsafeCallOnNullableType")
internal fun AdventSpec<*>.injectSolution(): Solution = this::class
    .starProjectedType.jvmErasure.supertypes
    .first { it.isSubtypeOf(typeOf<AdventSpec<*>>()) }
    .arguments.first().type!!.jvmErasure
    .run {
        this as KClass<Solution> // Must be a solution because of AdventSpec bounds.
        runCatching { newInstanceNoArgConstructorOrObjectInstance(this) }
            .getOrElse { throw MissingNoArgConstructorException(this) }
    }

/**
 * Defines the rootContext to test the implementation of one [part] of a [Solution].
 *
 * Will create a context with two tests:
 *  - Verifies the output, given the input file has been added to the test resources.
 *    If the solution is known as well, also validates the answer matches it.
 *  - Verifies the given examples in an [AdventPartScope], useful for a TDD approach when
 *    implementing the solution for the first time.
 *
 * @param part                The part selector.
 * @param enabled             If set to false, part one will not be tested.
 * @param expensive           This part is known to produce answers in a longer timespan.
 * @param executionMode       Specifies which tests defined for this part will be enabled.
 * @param efficiencyBenchmark The maximum amount of time a solution can take to finish to be considered efficient.
 * @param examples            Test the solution against example inputs defined in this [AdventPartScope].
 */
@Suppress("LongParameterList")
internal fun AdventSpec<*>.definePart(
    part: AdventDayPart,
    enabled: Boolean,
    expensive: Boolean,
    executionMode: ExecMode?,
    efficiencyBenchmark: Duration?,
    examples: (AdventPartScope.() -> Unit)?,
) {
    if (!definedParts.add(part)) throw DuplicatePartDefinitionException(this::class, part)

    context("Part $part").config(
        enabled = enabled,
        tags = if (expensive) setOf(Expensive) else emptySet(),
    ) {
        val partFunction = solution.partFunction(part)
        val config = configuration(efficiencyBenchmark, executionMode)

        if (examples != null) {
            defineExamples(config, partFunction, examples)
        }

        if (testData.input != null) {
            defineInput(
                config = config,
                expensive = expensive,
                partFunction = partFunction,
                input = testData.input,
                correctAnswer = testData.solutionToPart(part),
            )
        }
    }
}

/**
 * Define an example context, generating a separate test for each example given.
 * Examples are defined with the [AdventPartScope] DSL.
 *
 * @param config       The user's preferences for this test run.
 * @param partFunction The user's implementation of the solution.
 * @param examples     A configuration block defining the example values.
 */
@OptIn(ExperimentalKotest::class)
private suspend fun FunSpecContainerScope.defineExamples(
    config: AdventSpecConfig,
    partFunction: (String) -> Any,
    examples: (AdventPartScope.() -> Unit),
) {
    context(name = "Validates the examples").config(enabled = config.executionMode != ExecMode.SkipExamples) {
        AdventPartScopeImpl().apply(examples).forEachIndexed { index, input, expected ->
            test("Example #${index + 1}") {
                withClue("Expected answer '$expected' for input: ${input.preview()}") {
                    val answer = shouldNotThrowAny {
                        partFunction(input.toString()).toString()
                    }
                    answer shouldBe expected
                }
            }
        }
    }
}

/**
 * Define a context to test against the user's own input.
 * Generates up to three nested tests:
 * - The solution output, validated against the [correctAnswer] if known.
 * - Unverified solution, always ignored, only shows when the solution is computed but the [correctAnswer] is unknown.
 * - Efficiency benchmark, ignored if either the solution was not computed, the solution is unverified, or is marked as
 *   [expensive].
 *
 * @param config        The user's preferences for this test run.
 * @param expensive     Whether this solution is known to be inefficient.
 * @param partFunction  The user's implementation of the solution.
 * @param input         The user's custom puzzle input.
 * @param correctAnswer The actual puzzle solution, if known.
 */
@OptIn(ExperimentalKotest::class)
private suspend fun FunSpecContainerScope.defineInput(
    config: AdventSpecConfig,
    expensive: Boolean,
    partFunction: (String) -> Any,
    input: PuzzleInput,
    correctAnswer: PuzzleAnswer?,
) {
    context("The solution").config(enabled = config.executionMode != ExecMode.ExamplesOnly) {
        val solutionKnown = correctAnswer != null
        var answer: PuzzleAnswer? = null
        var duration: Duration? = null

        test(name = if (solutionKnown) "Is correct" else "Computes an answer") {
            runCatching {
                val (value, time) = measureTimedValue { partFunction(input.toString()) }
                answer = PuzzleAnswer(value.toString())
                duration = time
            }.onFailure { error ->
                AssertionErrorBuilder.create()
                    .withMessage("The solution threw an exception before it could return an answer.")
                    .withCause(error)
                    .build()
            }

            if (solutionKnown) {
                withClue("Got different answer than the known solution.") {
                    answer shouldBe correctAnswer
                }
            }
        }

        // If solution is unverified, create a dummy ignored test to display the value in the test report.
        if (!solutionKnown && answer != null) {
            xtest("Has unverified answer ($answer)") {}
        }

        val enableSpeedTesting = when {
            correctAnswer == null -> false
            answer != correctAnswer -> false
            expensive -> false
            else -> true
        }

        val benchmark = config.efficiencyBenchmark
        val durationSuffix = if (answer != null) duration.toString() else "N/A"
        test("Is reasonably efficient ($durationSuffix)").config(enabled = enableSpeedTesting) {
            withClue("The solution did not complete under the configured benchmark of $benchmark") {
                @Suppress("UnsafeCallOnNullableType")
                duration!! shouldBeLessThanOrEqualTo benchmark
            }
        }
    }
}
