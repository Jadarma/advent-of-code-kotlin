package io.github.jadarma.aockt.integration.feature

import io.github.jadarma.aockt.AdventDay
import io.github.jadarma.aockt.AdventSpec
import io.github.jadarma.aockt.integration.SampleCorrect
import io.github.jadarma.aockt.integration.testExtension
import io.github.jadarma.aockt.internal.MissingInputException
import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.shouldBe

private var didExecute = false

@AdventDay(9999, 2, "Debug Mode", "With input")
class DebugTestWithInput : AdventSpec<SampleCorrect>({
    partOne {
        "1" shouldOutput "wait, no it shouldn't output anything"
    }
    debug {
        didExecute = true
        solution shouldBe SampleCorrect
        input shouldBe "ABC"
    }
}) {

    init {
        testExtension(
            "Part One" to "Ignored",
            "Debug" to "Success",
        )
        beforeAny { testCase ->
            if(testCase.name.name == "Debug") {
                testCase.name.focus.shouldBeTrue()
                didExecute.shouldBeFalse()
            } else {
                testCase.name.focus.shouldBeFalse()
            }
        }
        afterTest { (testCase, result) ->
            result.isIgnored shouldBe (testCase.name.name != "Debug")
            if(testCase.name.name == "Debug") {
                didExecute.shouldBeTrue()
            }
        }
    }
}

@AdventDay(9999, 2, "Debug Mode", "No input")
class DebugTestWithoutInput : AdventSpec<SampleCorrect>({
    debug {
        shouldThrowExactly<MissingInputException>{ input }
    }
}) {
    init {
        testExtension(
            "Debug" to "Failure",
        )
    }
}
