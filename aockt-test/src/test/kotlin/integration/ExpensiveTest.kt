package io.github.jadarma.aockt.test.integration

import io.github.jadarma.aockt.test.AdventDay
import io.github.jadarma.aockt.test.AdventSpec
import io.github.jadarma.aockt.test.Expensive
import io.kotest.core.test.TestCase
import io.kotest.core.test.isRootTest
import io.kotest.core.test.parents
import io.kotest.engine.test.TestResult
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe

@AdventDay(9999, 2, "ExpensiveTest")
class ExpensiveTest : AdventSpec<ObjectSolution>({
    partOne { "A" shouldOutput "1:A" }
    partTwo(expensive = true) { "A" shouldOutput "2:1" }
}) {

    override suspend fun afterTest(testCase: TestCase, result: TestResult) {
        val isFromPartTwo = testCase.parents().firstOrNull()?.run { name.name == "Part Two" }
        val isEfficiencyTest = testCase.name.name.startsWith("Is reasonably efficient")
        if (isEfficiencyTest) result.isIgnored shouldBe isFromPartTwo
    }

    override suspend fun afterContainer(testCase: TestCase, result: TestResult) {
        if(testCase.isRootTest() && testCase.name.name == "Part Two") {
            testCase
                .config.shouldNotBeNull()
                .tags.shouldContain(Expensive)
        }
    }
}
