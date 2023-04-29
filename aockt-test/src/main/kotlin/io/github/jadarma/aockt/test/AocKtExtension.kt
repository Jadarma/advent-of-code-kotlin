package io.github.jadarma.aockt.test

import io.github.jadarma.aockt.test.internal.ConfigurationException
import io.github.jadarma.aockt.test.internal.configureAocKtDisplayNameExtension
import io.kotest.core.extensions.ProjectExtension
import io.kotest.core.project.ProjectContext
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

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
 * @property formatAdventSpecNames Whether to pretty print the names of the AdventSpec in the test output.
 *   Enabled by default.
 * @property efficiencyBenchmark What is the maximum runtime a solution can have while being considered efficient by
 *   the time tests.
 *   According to the AoC website, all solutions *should* be completable in 15 seconds regardless of hardware.
 *   You may decrease this value for an increased challenge though do be aware of fluctuations in execution time due to
 *   JVM warmup.
 *   Default is fifteen seconds.
 * @property executionMode The default execution mode for puzzle part definitions.
 *   If set to `ExamplesOnly`, does not run against the true puzzle input even if present.
 *   Useful when running the project with encrypted inputs (e.g. running a clone of someone else's solution repo).
 *   If set to `SkipExamples`, will only test against user input.
 *   Can be overridden for individual parts, see *Execution Configuration for Parts* for more details.
 */
public class AocKtExtension(
    private val formatAdventSpecNames: Boolean = true,
    internal val efficiencyBenchmark: Duration = defaultEfficiencyBenchmark,
    internal val executionMode: ExecMode = defaultExecutionMode,
) : ProjectExtension {

    init {
        require(efficiencyBenchmark.isPositive()) { "The efficiency benchmark must be positive." }
    }

    override suspend fun interceptProject(context: ProjectContext, callback: suspend (ProjectContext) -> Unit) {

        // Only allow a single instance of AocKt extension to be registered per project.
        checkConfig(context.configuration.registry.all().filterIsInstance<AocKtExtension>().first() === this) {
            "AocKtExtension was registered twice. Only one instance is allowed."
        }

        // Register a custom formatter
        if (formatAdventSpecNames) context.configureAocKtDisplayNameExtension()

        // Continue running the project.
        callback(context)
    }

    internal companion object {

        internal val defaultEfficiencyBenchmark: Duration = 15.seconds
        internal val defaultExecutionMode: ExecMode = ExecMode.All

        private fun checkConfig(condition: Boolean, lazyMessage: () -> String) {
            if (!condition) throw ConfigurationException(lazyMessage())
        }
    }
}

public enum class ExecMode {
    /** Run both tests and the user input, if available. */
    All,

    /** Do not run the user input, even if available. */
    ExamplesOnly,

    /** Do not run the defined examples. */
    SkipExamples;
}
