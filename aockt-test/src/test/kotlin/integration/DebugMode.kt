package io.github.jadarma.aockt.test.integration

import io.github.jadarma.aockt.test.AdventDay
import io.github.jadarma.aockt.test.AdventSpec
import io.kotest.core.test.TestCase
import io.kotest.engine.test.TestResult
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.shouldBe

private var didExecute = false

@AdventDay(3000, 6, "DebugMode")
class DebugMode : AdventSpec<ObjectSolution>({

    partOne {
        "1" shouldOutput "wait, no it shouldn't output anything"
    }

    debug {
        didExecute = true
    }
}) {

    override suspend fun beforeAny(testCase: TestCase) {
        if(testCase.name.name == "Debug") {
            testCase.name.focus.shouldBeTrue()
            didExecute.shouldBeFalse()
        } else {
            testCase.name.focus.shouldBeFalse()
        }
    }

    override suspend fun afterTest(testCase: TestCase, result: TestResult) {
        result.isIgnored shouldBe (testCase.name.name != "Debug")
        if(testCase.name.name == "Debug") {
            didExecute.shouldBeTrue()
        }
    }
}
