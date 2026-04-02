// Copyright © 2020 Dan Cîmpianu
// This code is licensed under the MIT license, detailed in LICENSE.md or at https://opensource.org/license/MIT.
package io.github.jadarma.aockt.internal

import io.github.jadarma.aockt.AdventDebugScope
import io.github.jadarma.aockt.AdventSpec
import io.github.jadarma.aockt.ExecMode
import io.github.jadarma.aockt.Solution
import kotlin.coroutines.AbstractCoroutineContextElement
import kotlin.coroutines.CoroutineContext
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

/**
 * Holds configurations for how an [AdventSpec] should behave by default when running tests.
 *
 * @property efficiencyBenchmark What is the maximum runtime a solution can have while being considered efficient by
 *                               the time tests.
 * @property executionMode       The default execution mode for puzzle part definitions.
 */
internal data class AdventProjectConfig(
    val efficiencyBenchmark: Duration,
    val executionMode: ExecMode,
) : AbstractCoroutineContextElement(Key) {

    init {
        if (efficiencyBenchmark.isPositive().not()) {
            throw ConfigurationException("Efficiency benchmark must be a positive value, but was: $efficiencyBenchmark")
        }
    }

    companion object Key : CoroutineContext.Key<AdventProjectConfig> {
        /** Sane defaults. */
        val Default: AdventProjectConfig = AdventProjectConfig(
            efficiencyBenchmark = 15.seconds,
            executionMode = ExecMode.All,
        )
    }
}

@Suppress("BooleanPropertyNaming", "DataClassContainsFunctions")
internal data class AdventTestConfig(
    val id: AdventDayID,
    val part: AdventDayPart,
    val partFunction: PartFunction,
    val enabled: Boolean,
    val expensive: Boolean,
    val executionMode: ExecMode?,
    val efficiencyBenchmark: Duration?,
    val examples: List<Pair<PuzzleInput, PuzzleAnswer>>,
) {

    data class ForExamples(
        val enabled: Boolean,
        val partFunction: PartFunction,
        val examples: List<Pair<PuzzleInput, PuzzleAnswer>>,
    )

    data class ForInput(
        val id: AdventDayID,
        val part: AdventDayPart,
        val enabled: Boolean,
        val partFunction: PartFunction,
        val efficiencyBenchmark: Duration,
    )

    internal fun forExamples(defaults: AdventProjectConfig) = ForExamples(
        enabled = if (!enabled) false else (executionMode ?: defaults.executionMode) != ExecMode.SkipExamples,
        partFunction = partFunction,
        examples = examples,
    )

    internal fun forInput(defaults: AdventProjectConfig) = ForInput(
        id = id,
        part = part,
        enabled = if (!enabled) false else (executionMode ?: defaults.executionMode) != ExecMode.ExamplesOnly,
        partFunction = partFunction,
        efficiencyBenchmark = efficiencyBenchmark ?: defaults.efficiencyBenchmark,
    )
}

internal data class AdventDebugConfig(
    val id: AdventDayID,
    val solution: Solution,
    val test: AdventDebugScope.() -> Unit,
)
