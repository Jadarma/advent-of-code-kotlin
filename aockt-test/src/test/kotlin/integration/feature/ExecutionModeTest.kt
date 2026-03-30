package io.github.jadarma.aockt.integration.feature

import io.github.jadarma.aockt.AdventDay
import io.github.jadarma.aockt.AdventSpec
import io.github.jadarma.aockt.ExecMode
import io.github.jadarma.aockt.integration.SampleCorrect
import io.github.jadarma.aockt.integration.testExtension

@AdventDay(9999, 4, "Execution Mode")
class ExecutionModeTest : AdventSpec<SampleCorrect>({
    partOne(executionMode = ExecMode.ExamplesOnly) {
            "hello" shouldOutput "1:hello"
            "pass" shouldOutput "1:pass"
    }
    partTwo(executionMode = ExecMode.SkipExamples) {
            "hello" shouldOutput "2:5"
            "pass" shouldOutput "2:4"
    }
}) {
    init {
        testExtension(
            "Part One -- Validates the examples -- Example #1" to "Success",
            "Part One -- Validates the examples -- Example #2" to "Success",
            "Part One -- The solution" to "Ignored",
            "Part Two -- Validates the examples" to "Ignored",
            "Part Two -- The solution -- Is correct" to "Success",
            "Part Two -- The solution -- Is reasonably efficient" to "Success",
        )
    }
}
