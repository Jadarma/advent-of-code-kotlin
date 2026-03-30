package io.github.jadarma.aockt.integration.feature

import io.github.jadarma.aockt.AdventDay
import io.github.jadarma.aockt.AdventSpec
import io.github.jadarma.aockt.integration.SampleExpensive
import io.github.jadarma.aockt.integration.testExtension
import io.github.jadarma.aockt.internal.Expensive
import io.kotest.core.test.TestCase
import io.kotest.core.test.isRootTest
import io.kotest.engine.test.TestResult
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.nulls.shouldNotBeNull

@AdventDay(9999, 4, "Expensive")
class ExpensiveTagTest : AdventSpec<SampleExpensive>({
    partOne(expensive = false)
    partTwo(expensive = true)
}) {
    init {
        testExtension(
            "Part One -- The solution -- Is correct" to "Success",
            "Part One -- The solution -- Is reasonably efficient" to "Failure",
            "Part Two -- The solution -- Is correct" to "Success",
            "Part Two -- The solution -- Is reasonably efficient" to "Ignored",
        )
    }

    override suspend fun afterContainer(testCase: TestCase, result: TestResult) {
        if(testCase.isRootTest() && testCase.name.name == "Part Two") {
            testCase
                .config.shouldNotBeNull()
                .tags.shouldContain(Expensive)
        }
    }
}
