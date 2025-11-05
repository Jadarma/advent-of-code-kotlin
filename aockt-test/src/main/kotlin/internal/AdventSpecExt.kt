package io.github.jadarma.aockt.test.internal

import io.github.jadarma.aockt.core.Solution
import io.github.jadarma.aockt.test.AdventDay
import io.github.jadarma.aockt.test.AdventSpec
import io.github.jadarma.aockt.test.Expensive
import io.kotest.assertions.AssertionErrorBuilder
import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.throwables.shouldNotThrowAnyUnit
import io.kotest.assertions.withClue
import io.kotest.common.ExperimentalKotest
import io.kotest.common.reflection.ReflectionInstantiations.newInstanceNoArgConstructorOrObjectInstance
import io.kotest.core.spec.style.scopes.FunSpecContainerScope
import io.kotest.core.test.parents
import io.kotest.matchers.comparables.shouldBeLessThanOrEqualTo
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.currentCoroutineContext
import kotlin.reflect.KClass
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.isSubtypeOf
import kotlin.reflect.full.starProjectedType
import kotlin.reflect.jvm.jvmErasure
import kotlin.reflect.typeOf
import kotlin.time.Duration
import kotlin.time.measureTimedValue

/** Return the required-to-be-registered [AdventDay] annotation for this spec. */
internal val KClass<out AdventSpec<*>>.adventDay: AdventDay
    get() = findAnnotation<AdventDay>() ?: throw MissingAdventDayAnnotationException(this)

/**
 * Construct and inject a solution instance for this [AdventSpec].
 * This function is designed to be called at spec instantiation time.
 * Uses reflection magic that while not that pretty, is fine for use in unit tests, and allows for a more elegant syntax
 * when declaring specs.
 */
@Suppress("UNCHECKED_CAST", "UnsafeCallOnNullableType")
internal fun KClass<out AdventSpec<*>>.injectSolution(): Solution = this
    .starProjectedType.jvmErasure.supertypes
    .first { it.isSubtypeOf(typeOf<AdventSpec<*>>()) }
    .arguments.first().type!!.jvmErasure
    .run {
        this as KClass<Solution> // Must be a solution because of AdventSpec bounds.
        runCatching { newInstanceNoArgConstructorOrObjectInstance(this) }
            .getOrElse { throw MissingNoArgConstructorException(this) }
    }

/**
 * Register a root context to test the implementation of one part of a [Solution].
 *
 * Will create two sub-contexts:
 *  - The examples, as defined by [registerExamples].
 *  - The solution, as defined by [registerInput].
 */
internal fun AdventSpec<*>.registerTest(config: AdventTestConfig): Unit = with(config) {
    context("Part $part").config(
        enabled = enabled,
        tags = if (expensive) setOf(Expensive) else null,
    ) {
        val projectConfig = currentCoroutineContext()[AdventProjectConfig.Key] ?: AdventProjectConfig.Default
        registerExamples(config.forExamples(projectConfig))
        registerInput(config.forInput(projectConfig))
    }
}

/**
 * Register a focused root test to help debugging a [Solution].
 * All other tests will be ignored.
 */
internal fun AdventSpec<*>.registerDebug(config: AdventDebugConfig): Unit = with(config) {
    test(name = "f:Debug") {
        val testData = TestData.inputFor(config.id)
        withClue("Debug run completed exceptionally.") {
            shouldNotThrowAnyUnit {
                AdventDebugScopeImpl(solution, testData.input).run(test)
            }
        }
    }
}

/** Define an example context, generating a separate test for each example given. */
@OptIn(ExperimentalKotest::class)
@Suppress("SuspendFunWithCoroutineScopeReceiver")
private suspend fun FunSpecContainerScope.registerExamples(config: AdventTestConfig.ForExamples): Unit = with(config) {
    if (examples.isEmpty()) return
    context(name = "Validates the examples").config(enabled = enabled) {
        examples.forEachIndexed { index, (input, expected) ->
            test("Example #${index + 1}") {
                withClue("Expected answer '$expected' for input: ${input.preview()}") {
                    val answer = shouldNotThrowAny { partFunction(input) }
                    answer shouldBe expected
                }
            }
        }
    }
}

/**
 * Define a context to test against the user's own input.
 * Sets up the following tests:
 * - The solution output is validated against the correct answer if known.
 * - Unverified solution, always ignored, only shows when the solution is computed but the correct answer is unknown.
 * - Efficiency benchmark, ignored if either the solution was not computed, the solution is unverified, or is marked as
 *   expensive.
 */
@OptIn(ExperimentalKotest::class)
@Suppress("SuspendFunWithCoroutineScopeReceiver", "CognitiveComplexMethod")
private suspend fun FunSpecContainerScope.registerInput(config: AdventTestConfig.ForInput): Unit = with(config) {

    val data = TestData.inputFor(config.id)
    val input = data.input ?: return
    val correctAnswer = data.solutionToPart(config.part)

    context("The solution").config(enabled = enabled) {
        val isSolutionKnown = correctAnswer != null
        var answer: PuzzleAnswer? = null
        var duration: Duration? = null

        test(name = if (isSolutionKnown) "Is correct" else "Computes an answer") {
            runCatching {
                val measured = measureTimedValue { partFunction(input) }
                answer = measured.value
                duration = measured.duration
            }.onFailure { error ->
                AssertionErrorBuilder.create()
                    .withMessage("The solution threw an exception before it could return an answer.")
                    .withCause(error)
                    .build()
            }

            if (isSolutionKnown) {
                withClue("Got different answer than the known solution.") {
                    answer shouldBe correctAnswer
                }
            }
        }

        // If solution is unverified, create a dummy ignored test to display the value in the test report.
        if (!isSolutionKnown && answer != null) {
            xtest("Has unverified answer ($answer)") {}
        }

        val enableSpeedTesting = when {
            correctAnswer == null -> false
            answer != correctAnswer -> false
            testCase.parents().any { Expensive in it.config?.tags.orEmpty() } -> false
            else -> true
        }

        val benchmark = config.efficiencyBenchmark
        val durationSuffix = duration?.takeIf { answer != null }?.toString() ?: "N/A"
        test("Is reasonably efficient ($durationSuffix)").config(enabled = enableSpeedTesting) {
            withClue("The solution did not complete under the configured benchmark of $benchmark") {
                @Suppress("UnsafeCallOnNullableType")
                duration!! shouldBeLessThanOrEqualTo benchmark
            }
        }
    }
}
