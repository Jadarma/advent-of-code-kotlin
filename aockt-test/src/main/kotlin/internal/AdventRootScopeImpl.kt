package io.github.jadarma.aockt.test.internal

import io.github.jadarma.aockt.core.Solution
import io.github.jadarma.aockt.test.AdventDebugScope
import io.github.jadarma.aockt.test.AdventPartScope
import io.github.jadarma.aockt.test.AdventRootScope
import io.github.jadarma.aockt.test.AdventSpec
import io.github.jadarma.aockt.test.ExecMode
import kotlin.reflect.KClass
import kotlin.time.Duration

internal class AdventRootScopeImpl(
    private val owner: KClass<out AdventSpec<*>>,
) : AdventRootScope {

    private val solution: Solution = owner.injectSolution()

    var partOne: AdventTestConfig? = null
    var partTwo: AdventTestConfig? = null
    var debug: AdventDebugConfig? = null

    override fun partOne(
        enabled: Boolean,
        executionMode: ExecMode?,
        efficiencyBenchmark: Duration?,
        expensive: Boolean,
        examples: AdventPartScope.() -> Unit
    ) {
        if (partOne != null) throw DuplicateDefinitionException(owner, "partOne")
        partOne = AdventTestConfig(
            part = AdventDayPart.One,
            partFunction = solution.partFunction(AdventDayPart.One),
            enabled = enabled,
            expensive = expensive,
            executionMode = executionMode,
            efficiencyBenchmark = efficiencyBenchmark,
            examples = AdventPartScopeImpl().apply(examples).testCases
        )
    }

    override fun partTwo(
        enabled: Boolean,
        executionMode: ExecMode?,
        efficiencyBenchmark: Duration?,
        expensive: Boolean,
        examples: AdventPartScope.() -> Unit
    ) {
        if (partTwo != null) throw DuplicateDefinitionException(owner, "partTwo")
        partTwo = AdventTestConfig(
            part = AdventDayPart.Two,
            partFunction = solution.partFunction(AdventDayPart.Two),
            enabled = enabled,
            expensive = expensive,
            executionMode = executionMode,
            efficiencyBenchmark = efficiencyBenchmark,
            examples = AdventPartScopeImpl().apply(examples).testCases
        )
    }

    override fun debug(test: AdventDebugScope.() -> Unit) {
        if (debug != null) throw DuplicateDefinitionException(owner, "debug")
        debug = AdventDebugConfig(solution, test)
    }
}
