package io.github.jadarma.aockt

import io.kotest.core.config.AbstractProjectConfig
import io.kotest.core.extensions.Extension
import io.kotest.core.spec.SpecExecutionOrder
import io.kotest.engine.concurrency.SpecExecutionMode
import kotlin.time.Duration.Companion.milliseconds

@Suppress("Unused")
object TestConfig : AbstractProjectConfig() {

    override val extensions = listOf<Extension>(
        AocKtExtension(
            efficiencyBenchmark = 100.milliseconds,
        )
    )

    override val specExecutionMode = SpecExecutionMode.LimitedConcurrency(Runtime.getRuntime().availableProcessors())
    override val specExecutionOrder = SpecExecutionOrder.Lexicographic
}
