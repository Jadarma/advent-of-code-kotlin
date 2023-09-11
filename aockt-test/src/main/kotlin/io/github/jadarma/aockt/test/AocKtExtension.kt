package io.github.jadarma.aockt.test

import io.github.jadarma.aockt.test.internal.AocktDisplayNameFormatter
import io.github.jadarma.aockt.test.internal.ConfigurationException
import io.kotest.core.config.ProjectConfiguration
import io.kotest.core.extensions.DisplayNameFormatterExtension
import io.kotest.core.extensions.Extension
import io.kotest.core.extensions.ProjectExtension
import io.kotest.core.names.DisplayNameFormatter
import io.kotest.core.project.ProjectContext
import io.kotest.engine.config.ConfigManager
import io.kotest.engine.config.detectAbstractProjectConfigsJVM
import io.kotest.engine.config.loadProjectConfigFromClassnameJVM
import io.kotest.engine.test.names.DefaultDisplayNameFormatter
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
 *   Can be overridden on a per-test basis.
 *   According to the AoC website, all solutions *should* be completable in 15 seconds regardless of hardware.
 *   You may decrease this value for an increased challenge though do be aware of fluctuations in execution time due to
 *   JVM warmup.
 *   Default is fifteen seconds.
 * @property executionMode The default execution mode for puzzle part definitions.
 *   Can be overridden on a per-test basis.
 *   If set to `ExamplesOnly`, does not run against the true puzzle input even if present.
 *   Useful when running the project with encrypted inputs (e.g. running a clone of someone else's solution repo).
 *   If set to `SkipExamples`, will only test against user input.
 *   Default is `All`.
 */
public class AocKtExtension(
    private val formatAdventSpecNames: Boolean = true,
    internal val efficiencyBenchmark: Duration = defaultEfficiencyBenchmark,
    internal val executionMode: ExecMode = defaultExecutionMode,
) : ProjectExtension, DisplayNameFormatterExtension {

    @Suppress("ForbiddenComment")
    // TODO: This is an experimental hack to enable properly configured fallbacks for formatting, but it currently
    //       relies on internal APIs.
    //       [Kotest#3679](https://github.com/kotest/kotest/issues/3679) tracks the feature request to work around this.
    private val displayNameFormatter: DisplayNameFormatter by lazy {

        // Get the materialized configuration. Unfortunately relies on internal APIs and has to be retested on every
        // new release.
        @Suppress("UnstableApiUsage")
        val configuration: ProjectConfiguration = ConfigManager.initialize(ProjectConfiguration()) {
            detectAbstractProjectConfigsJVM() + listOfNotNull(loadProjectConfigFromClassnameJVM())
        }

        // See if any other formatter extensions have been registered by the user.
        val registeredFormatters: List<DisplayNameFormatterExtension> =
            configuration
                .registry.all()
                .filterIsInstance<DisplayNameFormatterExtension>()
                .filterNot { it is AocKtExtension }

        // Get the fallback formatter, which is either the user provided one, or the default formatter based on the
        // user-customised configuration.
        val fallbackFormatter = registeredFormatters
            .firstOrNull()
            ?.formatter()
            ?: DefaultDisplayNameFormatter(configuration)

        // Unregister all other formatter extensions, so that Kotest's call of `getDisplayNameFormatter()` is
        // guaranteed to return a reference to this extension.
        // This is okay to do because we already have a reference to the fallback formatter, and formatting extensions
        // are only used to retrieve it anyway.
        registeredFormatters.forEach {
            configuration.registry.remove(it as Extension)
        }

        // If custom AdventSpec formatting is not desired, return the fallback right away.
        // Otherwise, wrap it in the custom formatter.
        when (formatAdventSpecNames) {
            false -> fallbackFormatter
            true -> AocktDisplayNameFormatter(fallbackFormatter)
        }
    }

    override fun formatter(): DisplayNameFormatter = displayNameFormatter

    init {
        require(efficiencyBenchmark.isPositive()) { "The efficiency benchmark must be positive." }
    }

    override suspend fun interceptProject(context: ProjectContext, callback: suspend (ProjectContext) -> Unit) {

        // Only allow a single instance of AocKt extension to be registered per project.
        checkConfig(context.configuration.registry.all().filterIsInstance<AocKtExtension>().first() === this) {
            "AocKtExtension was registered twice. Only one instance is allowed."
        }

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
