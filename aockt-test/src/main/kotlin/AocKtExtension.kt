package io.github.jadarma.aockt.test

import io.github.jadarma.aockt.test.internal.AdventProjectConfig
import io.github.jadarma.aockt.test.internal.AocKtDisplayNameFormatter
import io.github.jadarma.aockt.test.internal.SpecOrderer
import io.kotest.core.extensions.DisplayNameFormatterExtension
import io.kotest.core.extensions.SpecExecutionOrderExtension
import io.kotest.core.extensions.SpecExtension
import io.kotest.core.spec.Spec
import io.kotest.core.spec.SpecRef
import io.kotest.engine.names.DisplayNameFormatter
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.withContext
import kotlin.time.Duration

/**
 * A Kotest Extension to configure the AdventSpecs.
 *
 * To register the extension:
 *
 * ```kotlin
 * object TestConfig : AbstractProjectConfig() {
 *     override val extensions = listOf<Extension>(AocKtExtension())
 * }
 * ```
 *
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
    efficiencyBenchmark: Duration = AdventProjectConfig.Default.efficiencyBenchmark,
    executionMode: ExecMode = AdventProjectConfig.Default.executionMode,
) : DisplayNameFormatterExtension, SpecExecutionOrderExtension, SpecExtension {

    /** The project-level config that will apply to all [AdventSpec]s. */
    internal val configuration: AdventProjectConfig = AdventProjectConfig(efficiencyBenchmark, executionMode)

    /** Provide the custom formatter to the extension. */
    override fun formatter(): DisplayNameFormatter = AocKtDisplayNameFormatter

    /** Provide the custom execution order. */
    override fun sort(specs: List<SpecRef>): List<SpecRef> = specs.sortedWith(SpecOrderer)

    /** Provide project-level config to scopes of all advent specs. */
    override suspend fun intercept(
        spec: Spec,
        execute: suspend (Spec) -> Unit,
    ) {
        if (spec is AdventSpec<*>) {
            withContext(currentCoroutineContext() + configuration) { execute(spec) }
        } else {
            execute(spec)
        }
    }
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
