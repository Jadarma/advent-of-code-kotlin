package io.github.jadarma.aockt.test

import io.github.jadarma.aockt.core.Solution
import io.github.jadarma.aockt.test.internal.AdventDayID
import io.github.jadarma.aockt.test.internal.AdventDayPart
import io.github.jadarma.aockt.test.internal.AdventDayPart.One
import io.github.jadarma.aockt.test.internal.AdventDayPart.Two
import io.github.jadarma.aockt.test.internal.AdventSpecPartScope
import io.github.jadarma.aockt.test.internal.AocktDsl
import io.github.jadarma.aockt.test.internal.ConfigurationException
import io.github.jadarma.aockt.test.internal.DuplicatePartDefinitionException
import io.github.jadarma.aockt.test.internal.MissingAdventDayAnnotationException
import io.github.jadarma.aockt.test.internal.MissingNoArgConstructorException
import io.github.jadarma.aockt.test.internal.PuzzleAnswer
import io.github.jadarma.aockt.test.internal.PuzzleTestData
import io.github.jadarma.aockt.test.internal.TestData
import io.github.jadarma.aockt.test.internal.configuration
import io.github.jadarma.aockt.test.internal.id
import io.github.jadarma.aockt.test.internal.partFunction
import io.github.jadarma.aockt.test.internal.solutionToPart
import io.kotest.assertions.AssertionErrorBuilder
import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.withClue
import io.kotest.common.ExperimentalKotest
import io.kotest.common.reflection.ReflectionInstantiations.newInstanceNoArgConstructorOrObjectInstance
import io.kotest.common.reflection.annotation
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.FunSpec
import io.kotest.core.test.TestCaseOrder
import io.kotest.engine.concurrency.TestExecutionMode
import io.kotest.engine.coroutines.CoroutineDispatcherFactory
import io.kotest.engine.coroutines.ThreadPerSpecCoroutineContextFactory
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
 * @param T The implementation class of the [Solution] to be tested.
 * @param body A context in which to configure the tests.
 */
@OptIn(ExperimentalKotest::class)
@AocktDsl
public abstract class AdventSpec<T : Solution>(
    body: AdventSpec<T>.() -> Unit = {},
) : FunSpec() {

    private val adventDayId: AdventDayID
    private val testData: PuzzleTestData

    // Injected by some reflection magic that while not that pretty, is fine for use in unit tests, and allows for a
    // more elegant syntax when declaring [AdventSpec]s.
    /** The instance of the [Solution] to be tested. */
    @Suppress("MemberVisibilityCanBePrivate")
    public val solution: Solution = this::class
        .starProjectedType.jvmErasure.supertypes
        .first { it.isSubtypeOf(typeOf<AdventSpec<*>>()) }
        .arguments.first().type!!.jvmErasure
        .run {
            @Suppress("UNCHECKED_CAST") // Must be a solution because of AdventSpec bounds.
            this as KClass<Solution>

            runCatching { newInstanceNoArgConstructorOrObjectInstance(this) }
                .getOrElse { throw MissingNoArgConstructorException(this) }
        }

    // Enforce some configuration to ensure that all tests within one AdventSpec will be executed sequentially on a
    // single thread.
    final override fun coroutineDispatcherFactory(): CoroutineDispatcherFactory = ThreadPerSpecCoroutineContextFactory
    final override fun isolationMode(): IsolationMode = IsolationMode.SingleInstance
    final override fun testCaseOrder(): TestCaseOrder = TestCaseOrder.Sequential
    final override fun testExecutionMode(): TestExecutionMode = TestExecutionMode.Sequential

    init {
        val adventDay = this::class.annotation<AdventDay>() ?: throw MissingAdventDayAnnotationException(this::class)
        adventDayId = adventDay.id
        testData = TestData.inputFor(adventDayId)
        body()
    }

    // Flags to prevent the user from defining a part more than once.
    private var isPartOneDefined: Boolean = false
    private var isPartTwoDefined: Boolean = false

    /** A DSL scope for defining example assertions for puzzle parts. */
    @AocktDsl
    public interface PartScope {

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

    /**
     * Provides a context to test the implementation of one of a [Solution]'s part function.
     *
     * Will create a context with two tests:
     *  - Verifies the output, given the input file has been added to the test resources.
     *    If the solution is known as well, also validates the answer matches it.
     *  - Verifies the given examples in an [AdventSpec.PartScope], useful for a TDD approach when
     *    implementing the solution for the first time.
     *
     * @param part The part selector.
     * @param enabled If set to false, part one will not be tested.
     * @param expensive This part is known to produce answers in a longer timespan.
     * @param executionMode Specifies which tests defined for this part will be enabled.
     * @param efficiencyBenchmark The maximum amount of time a solution can take to finish to be considered efficient.
     * @param examples Test the solution against example inputs defined in this [AdventSpec.PartScope].
     */
    @Suppress("LongParameterList", "ThrowsCount", "LongMethod", "CyclomaticComplexMethod")
    private fun partTest(
        part: AdventDayPart,
        enabled: Boolean,
        expensive: Boolean,
        executionMode: ExecMode?,
        efficiencyBenchmark: Duration?,
        examples: (PartScope.() -> Unit)?,
    ) {
        if (efficiencyBenchmark != null && !efficiencyBenchmark.isPositive()) {
            throw ConfigurationException("Efficiency benchmark must be a positive value, but was: $efficiencyBenchmark")
        }

        when (part) {
            One -> {
                if (isPartOneDefined) throw DuplicatePartDefinitionException(this::class, One)
                isPartOneDefined = true
            }

            Two -> {
                if (isPartTwoDefined) throw DuplicatePartDefinitionException(this::class, Two)
                isPartTwoDefined = true
            }
        }

        context("Part $part").config(
            enabled = enabled,
            tags = if (expensive) setOf(Expensive) else emptySet(),
        ) {
            val partFunction = solution.partFunction(part)

            val config = configuration()
            val execMode = executionMode ?: config.executionMode
            val maxEfficientDuration = efficiencyBenchmark ?: config.efficiencyBenchmark

            if (examples != null) {
                context("Validates the examples").config(enabled = execMode != ExecMode.SkipExamples) {
                    AdventSpecPartScope().apply(examples).forEachIndexed { index, input, expected ->
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

            context("The solution").config(
                enabled = testData.input != null && execMode != ExecMode.ExamplesOnly,
            ) {
                val input = testData.input?.toString()
                checkNotNull(input) { "Impossible state, test should be disabled on null input." }

                val correctAnswer = testData.solutionToPart(part)
                val solutionKnown = correctAnswer != null

                var answer: PuzzleAnswer? = null
                var duration: Duration? = null

                val mainTestName = if (solutionKnown) "Is correct" else "Computes an answer"
                test(mainTestName) {

                    runCatching {
                        val (value, time) = measureTimedValue { partFunction(input) }
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
                    efficiencyBenchmark != null -> true
                    expensive -> false
                    else -> true
                }
                val durationSuffix = if (answer != null) duration.toString() else "N/A"
                test("Is reasonably efficient ($durationSuffix)").config(enabled = enableSpeedTesting) {
                    withClue("The solution did not complete under the configured benchmark of $maxEfficientDuration") {
                        duration!! shouldBeLessThanOrEqualTo maxEfficientDuration
                    }
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
     *  - Verifies the given examples in an [AdventSpec.PartScope], useful for a TDD approach when
     *    implementing the solution for the first time.
     *
     * @param enabled If set to false, part one will not be tested.
     * @param expensive This part is known to produce answers in a longer timespan.
     * @param executionMode Specifies which tests defined for this part will be enabled.
     * @param efficiencyBenchmark The maximum amount of time a solution can take to finish to be considered efficient.
     * @param test Test the solution against example inputs defined in this [AdventSpec.PartScope].
     */
    public fun partOne(
        enabled: Boolean = true,
        expensive: Boolean = false,
        executionMode: ExecMode? = null,
        efficiencyBenchmark: Duration? = null,
        test: (PartScope.() -> Unit)? = null,
    ): Unit = partTest(One, enabled, expensive, executionMode, efficiencyBenchmark, test)

    /**
     * Provides a context to test the implementation a [Solution.partTwo] function.
     * Should only be called once per [AdventSpec].
     *
     * Will create a context with two tests:
     *  - Verifies the output, given the input file has been added to the test resources.
     *    If the solution is known as well, also validates the answer matches it.
     *  - Verifies the given examples in an [AdventSpec.PartScope], useful for a TDD approach when
     *    implementing the solution for the first time.
     *
     * @param enabled If set to false, part one will not be tested.
     * @param expensive This part is known to produce answers in a longer timespan.
     * @param executionMode Specifies which tests defined for this part will be enabled.
     * @param efficiencyBenchmark The maximum amount of time a solution can take to finish to be considered efficient.
     * @param test Test the solution against example inputs defined in this [AdventSpec.PartScope].
     */
    public fun partTwo(
        enabled: Boolean = true,
        expensive: Boolean = false,
        executionMode: ExecMode? = null,
        efficiencyBenchmark: Duration? = null,
        test: (PartScope.() -> Unit)? = null,
    ): Unit = partTest(Two, enabled, expensive, executionMode, efficiencyBenchmark, test)
}
