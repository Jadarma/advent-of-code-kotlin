package io.github.jadarma.aockt.test.integration

import io.github.jadarma.aockt.core.Solution
import io.github.jadarma.aockt.test.AdventDay
import io.github.jadarma.aockt.test.AdventSpec
import io.github.jadarma.aockt.test.ExecMode
import io.kotest.assertions.AssertionErrorBuilder.Companion.fail

class OnlyAcceptsExamples : Solution {

    override fun partOne(input: String) = when (input) {
        "1,2,3" -> 4
        "1,2,3,4,5,6,7,8,9,10" -> fail("Should not run against user input.")
        else -> fail("Misconfigured test.")
    }

    override fun partTwo(input: String) = when (input) {
        "1,2,3" -> fail("Should not run against examples.")
        "1,2,3,4,5,6,7,8,9,10" -> 3_628_800
        else -> fail("Misconfigured test.")
    }
}

@AdventDay(9999, 1, "Magic Numbers", "Testing Execution Modes")
class ExecutionModesTest : AdventSpec<OnlyAcceptsExamples>({
    partOne(executionMode = ExecMode.ExamplesOnly) {
        "1,2,3" shouldOutput 4
    }
    partTwo(executionMode = ExecMode.SkipExamples) {
        "1,2,3" shouldOutput 6
    }
})
