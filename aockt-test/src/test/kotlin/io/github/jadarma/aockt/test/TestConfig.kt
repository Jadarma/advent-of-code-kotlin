package io.github.jadarma.aockt.test

import io.kotest.common.ExperimentalKotest
import io.kotest.core.config.AbstractProjectConfig
import io.kotest.core.extensions.Extension
import kotlin.time.Duration.Companion.milliseconds

@Suppress("Unused")
@ExperimentalKotest
object TestConfig : AbstractProjectConfig() {

    override fun extensions() = listOf<Extension>(
        AocKtExtension(
            formatAdventSpecNames = true,
            efficiencyBenchmark = 100.milliseconds,
        )
    )

    private val cores = Runtime.getRuntime().availableProcessors()
    override val parallelism: Int = cores
    override val concurrentSpecs: Int = cores
}
