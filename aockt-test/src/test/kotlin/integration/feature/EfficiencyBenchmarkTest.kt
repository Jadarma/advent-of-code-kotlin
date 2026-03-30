package io.github.jadarma.aockt.integration.feature

import io.github.jadarma.aockt.AdventDay
import io.github.jadarma.aockt.AdventSpec
import io.github.jadarma.aockt.integration.SampleExpensive
import io.github.jadarma.aockt.integration.testExtension
import kotlin.time.Duration.Companion.milliseconds

@AdventDay(9999, 4, "Efficiency Benchmark")
class EfficiencyBenchmarkTest : AdventSpec<SampleExpensive>({
    partOne()
    partTwo(efficiencyBenchmark = 200.milliseconds)
}) {
    init {
        testExtension(
            "Part One -- The solution -- Is correct" to "Success",
            "Part One -- The solution -- Is reasonably efficient" to "Failure",
            "Part Two -- The solution -- Is correct" to "Success",
            "Part Two -- The solution -- Is reasonably efficient" to "Success",
        )
    }
}
