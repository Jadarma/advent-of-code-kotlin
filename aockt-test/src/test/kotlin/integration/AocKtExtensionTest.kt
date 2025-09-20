package io.github.jadarma.aockt.test.integration

import io.github.jadarma.aockt.test.AdventDay
import io.github.jadarma.aockt.test.AdventSpec
import io.github.jadarma.aockt.test.AocKtExtension
import io.github.jadarma.aockt.test.ExecMode
import io.github.jadarma.aockt.test.internal.AdventProjectConfig
import io.github.jadarma.aockt.test.internal.ConfigurationException
import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.currentCoroutineContext
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

@AdventDay(3000, 5, "AocKtExtensionTest")
class AocKtExtensionTest : AdventSpec<ObjectSolution>() {

    init {
        context("Applies config") {
            test("and validates it") {
                shouldThrowExactly<ConfigurationException> { AocKtExtension(efficiencyBenchmark = (-1).seconds) }
                    .message
                    .shouldBe("Efficiency benchmark must be a positive value, but was: -1s")
            }

            test("by default") {
                AocKtExtension().configuration shouldBe AdventProjectConfig.Default
            }

            test("and applies overrides") {
                AocKtExtension(executionMode = ExecMode.ExamplesOnly).configuration shouldBe AdventProjectConfig(
                    efficiencyBenchmark = AdventProjectConfig.Default.efficiencyBenchmark,
                    executionMode = ExecMode.ExamplesOnly,
                )
            }
        }

        test("Can be read from advent specs") {
            currentCoroutineContext()[AdventProjectConfig.Key]
                .shouldNotBeNull()
                .should { config ->
                    config.executionMode shouldBe ExecMode.All
                    config.efficiencyBenchmark shouldBe 100.milliseconds
                }
        }
    }
}
