package io.github.jadarma.aockt.test

import io.github.jadarma.aockt.test.internal.AdventSpecConfig
import io.github.jadarma.aockt.test.internal.AocktDisplayNameFormatter
import io.kotest.core.extensions.DisplayNameFormatterExtension
import io.kotest.core.extensions.TestCaseExtension
import io.kotest.core.names.DisplayNameFormatter
import io.kotest.core.test.TestCase
import io.kotest.core.test.TestResult
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.withContext
import kotlin.coroutines.AbstractCoroutineContextElement
import kotlin.coroutines.CoroutineContext
import kotlin.time.Duration

/**
 * A Kotest Extension to configure the AdventSpecs.
 *
 * To register the extension:
 *
 * ```kotlin
 * object TestConfig : AbstractProjectConfig() {
 *     override fun extensions() = listOf<Extension>(AocktExtension())
 * }
 * ```
 *
 * @param formatAdventSpecNames Whether to pretty print the names of the AdventSpec in the test output.
 *   Enabled by default.
 * @param efficiencyBenchmark What is the maximum runtime a solution can have while being considered efficient by
 *   the time tests.
 *   Can be overridden on a per-test basis.
 *   According to the AoC website, all solutions *should* be completable in 15 seconds regardless of hardware.
 *   You may decrease this value for an increased challenge though do be aware of fluctuations in execution time due to
 *   JVM warmup.
 *   Default is fifteen seconds.
 * @param executionMode The default execution mode for puzzle part definitions.
 *   Can be overridden on a per-test basis.
 *   If set to `ExamplesOnly`, does not run against the true puzzle input even if present.
 *   Useful when running the project with encrypted inputs (e.g. running a clone of someone else's solution repo).
 *   If set to `SkipExamples`, will only test against user input.
 *   Default is `All`.
 */
public class AocKtExtension(
    private val formatAdventSpecNames: Boolean = true,
    efficiencyBenchmark: Duration = AdventSpecConfig.Default.efficiencyBenchmark,
    executionMode: ExecMode = AdventSpecConfig.Default.executionMode,
) : TestCaseExtension, DisplayNameFormatterExtension, AbstractCoroutineContextElement(Key) {

    internal val configuration: AdventSpecConfig = AdventSpecConfig(efficiencyBenchmark, executionMode)

    private val displayNameFormatter = AocktDisplayNameFormatter(disabled = formatAdventSpecNames.not())

    override fun formatter(): DisplayNameFormatter = displayNameFormatter

    override suspend fun intercept(testCase: TestCase, execute: suspend (TestCase) -> TestResult): TestResult =
        if (testCase.spec is AdventSpec<*>) {
            withContext(currentCoroutineContext() + this) { execute(testCase) }
        } else {
            execute(testCase)
        }

    internal companion object Key : CoroutineContext.Key<AocKtExtension>
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
