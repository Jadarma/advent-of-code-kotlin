package io.github.jadarma.aockt.test

import io.github.jadarma.aockt.core.Solution
import io.github.jadarma.aockt.test.internal.AdventDayID
import io.github.jadarma.aockt.test.internal.AdventDayPart
import io.github.jadarma.aockt.test.internal.AdventDayPart.One
import io.github.jadarma.aockt.test.internal.AdventDayPart.Two
import io.github.jadarma.aockt.test.internal.ConflictingPartExampleConfigurationException
import io.github.jadarma.aockt.test.internal.DuplicatePartDefinitionException
import io.github.jadarma.aockt.test.internal.MissingAdventDayAnnotationException
import io.github.jadarma.aockt.test.internal.MissingNoArgConstructorException
import io.github.jadarma.aockt.test.internal.PuzzleAnswer
import io.github.jadarma.aockt.test.internal.PuzzleTestData
import io.github.jadarma.aockt.test.internal.TestData
import io.github.jadarma.aockt.test.internal.id
import io.github.jadarma.aockt.test.internal.partFunction
import io.github.jadarma.aockt.test.internal.solutionToPart
import io.kotest.assertions.failure
import io.kotest.assertions.withClue
import io.kotest.common.ExperimentalKotest
import io.kotest.core.concurrency.CoroutineDispatcherFactory
import io.kotest.core.config.ProjectConfiguration
import io.kotest.core.config.configuration
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.FunSpec
import io.kotest.core.test.TestCaseOrder
import io.kotest.matchers.comparables.shouldBeLessThanOrEqualTo
import io.kotest.matchers.shouldBe
import io.kotest.mpp.annotation
import io.kotest.mpp.newInstanceNoArgConstructorOrObjectInstance
import kotlin.reflect.KClass
import kotlin.reflect.full.isSubtypeOf
import kotlin.reflect.full.starProjectedType
import kotlin.reflect.jvm.jvmErasure
import kotlin.reflect.typeOf
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes
import kotlin.time.ExperimentalTime
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
@OptIn(ExperimentalKotest::class, ExperimentalTime::class)
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

            runCatching { newInstanceNoArgConstructorOrObjectInstance() }
                .getOrElse { throw MissingNoArgConstructorException(this) }
        }

    // Enforce some configuration to ensure that all tests within one AdventSpec will be executed sequentially on a
    // single thread.
    final override fun isolationMode(): IsolationMode = IsolationMode.SingleInstance
    final override fun testCaseOrder(): TestCaseOrder = TestCaseOrder.Sequential
    final override fun concurrency(): Int = ProjectConfiguration.Sequential
    final override fun threads(): Int = 1
    final override fun dispatcherAffinity(): Boolean = true
    final override fun coroutineDispatcherFactory(): CoroutineDispatcherFactory? = null

    init {
        val adventDay = this::class.annotation<AdventDay>() ?: throw MissingAdventDayAnnotationException(this::class)
        adventDayId = adventDay.id
        testData = TestData.inputFor(adventDayId)
        body()
    }

    // Flags to prevent the user from defining a part more than once.
    private var isPartOneDefined: Boolean = false
    private var isPartTwoDefined: Boolean = false

    /**
     * Provides a context to test the implementation of one of a [Solution]'s part function.
     *
     * Will create a context with two tests:
     *  - Verifies the output, given the input file has been added to the test resources.
     *    If the solution is known as well, also validates the answer matches it.
     *  - Verifies the given examples in an [AdventSpecExampleContainerScope], useful for a TDD approach when
     *    implementing the solution for the first time.
     *
     * @param part The part selector.
     * @param enabled If set to false, part one will not be tested.
     * @param expensive This part is known to produce answers in a longer timespan.
     * @param examplesOnly Only run the examples, and don't test against actual input.
     * @param skipExamples Only run against actual input.
     * @param examples Test the solution against example inputs defined in this [AdventSpecExampleContainerScope].
     */
    @Suppress("LongParameterList", "ThrowsCount", "LongMethod", "CyclomaticComplexMethod")
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

            if (examples != null) {
                context("Validates the examples").config(enabled = !skipExamples) {
                    AdventSpecExampleContainerScope(partFunction, this).examples()
                }
            }

            context("The solution").config(
                enabled = testData.input != null && !examplesOnly,
                timeout = 1.minutes.takeUnless { expensive },
                blockingTest = true,
            ) {
                val input = testData.input?.toString()
                checkNotNull(input) { "Impossible state, test should be disabled on null input." }

                val correctAnswer = testData.solutionToPart(part)
                val solutionKnown = correctAnswer != null

                var error: Throwable? = null
                var answer: PuzzleAnswer? = null
                var duration: Duration? = null

                runCatching {
                    val (value, time) = measureTimedValue { partFunction(input) }
                    answer = PuzzleAnswer(value.toString())
                    duration = time
                }.onFailure { error = it }

                val unverifiedSuffix = if(!solutionKnown) " (Unverified: $answer)" else ""
                test("Is correct$unverifiedSuffix").config(enabled = solutionKnown) {
                    if(error != null) {
                        throw failure("The solution threw an exception before it could return an answer.", error)
                    }

                    withClue("Got different answer than the known solution.") {
                        answer shouldBe correctAnswer
                    }
                }

                val durationSuffix = if(error == null) " ($duration)" else ""
                test("Is reasonably efficient$durationSuffix").config(enabled = !expensive) {
                    withClue("Every problem has a solution that completes in at most 15s.") {
                        val efficiencyBenchmark = configuration.registry.all()
                            .filterIsInstance<AocKtExtension>()
                            .firstOrNull()
                            ?.efficiencyBenchmark
                            ?: AocKtExtension.defaultEfficiencyBenchmark

                        duration!! shouldBeLessThanOrEqualTo efficiencyBenchmark
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
