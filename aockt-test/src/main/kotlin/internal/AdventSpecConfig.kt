package io.github.jadarma.aockt.test.internal

import io.github.jadarma.aockt.test.AdventSpec
import io.github.jadarma.aockt.test.AocKtExtension
import io.github.jadarma.aockt.test.ExecMode
import kotlinx.coroutines.currentCoroutineContext
import kotlin.coroutines.AbstractCoroutineContextElement
import kotlin.coroutines.CoroutineContext
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

/**
 * Holds configurations for how an [AdventSpec] should behave when running tests.
 *
 * @property efficiencyBenchmark What is the maximum runtime a solution can have while being considered efficient by
 *                               the time tests.
 * @property executionMode       The default execution mode for puzzle part definitions.
 */
internal data class AdventSpecConfig(
    val efficiencyBenchmark: Duration,
    val executionMode: ExecMode,
) : AbstractCoroutineContextElement(Key) {

    init {
        if (efficiencyBenchmark.isPositive().not()) {
            throw ConfigurationException("Efficiency benchmark must be a positive value, but was: $efficiencyBenchmark")
        }
    }

    companion object Key : CoroutineContext.Key<AdventSpecConfig> {
        /** Sane defaults. */
        val Default: AdventSpecConfig = AdventSpecConfig(
            efficiencyBenchmark = 15.seconds,
            executionMode = ExecMode.All,
        )
    }
}

/**
 * Retrieves the [AdventSpecConfig] for this spec from the test runner coroutine.
 * If an [AocKtExtension] has been registered, use the user-provided configuration.
 * Otherwise, returns the sane defaults.
 */
@Suppress("UnusedReceiverParameter")
internal suspend fun AdventSpec<*>.configuration(): AdventSpecConfig =
    currentCoroutineContext()[AdventSpecConfig.Key] ?: AdventSpecConfig.Default
