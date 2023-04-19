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
 */
public class AocKtExtension(
    private val formatAdventSpecNames: Boolean = true,
    internal val efficiencyBenchmark: Duration = defaultEfficiencyBenchmark,
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

        private fun checkConfig(condition: Boolean, lazyMessage: () -> String) {
            if (!condition) throw ConfigurationException(lazyMessage())
        }
    }
}
