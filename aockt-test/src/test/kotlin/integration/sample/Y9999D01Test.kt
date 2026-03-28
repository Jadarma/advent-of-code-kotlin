package io.github.jadarma.aockt.test.integration.sample

import io.github.jadarma.aockt.test.AdventDay
import io.github.jadarma.aockt.test.integration.*

@AdventDay(9999, 1, "Sample", "Empty")
class Y9999D01EmptyTest : SampleSpec<SampleEmpty>() {
    init {
        testExtension(
            "Part One -- Validates the examples -- Example #1" to "Failure",
            "Part One -- Validates the examples -- Example #2" to "Failure",
            "Part Two -- Validates the examples -- Example #1" to "Failure",
            "Part Two -- Validates the examples -- Example #2" to "Failure",
        )
    }
}

@AdventDay(9999, 1, "Sample", "Partial")
class Y9999D01PartialTest : SampleSpec<SamplePartial>() {
    init {
        testExtension(
            "Part One -- Validates the examples -- Example #1" to "Success",
            "Part One -- Validates the examples -- Example #2" to "Success",
            "Part Two -- Validates the examples -- Example #1" to "Failure",
            "Part Two -- Validates the examples -- Example #2" to "Failure",
        )
    }
}

@AdventDay(9999, 1, "Sample", "Correct")
class Y9999D01CorrectTest : SampleSpec<SampleCorrect>() {
    init {
        testExtension(
            "Part One -- Validates the examples -- Example #1" to "Success",
            "Part One -- Validates the examples -- Example #2" to "Success",
            "Part Two -- Validates the examples -- Example #1" to "Success",
            "Part Two -- Validates the examples -- Example #2" to "Success",
        )
    }
}

@AdventDay(9999, 1, "Sample", "OnlyExample")
class Y9999D01OnlyExampleTest : SampleSpec<SampleOnlyExample>() {
    init {
        testExtension(
            "Part One -- Validates the examples -- Example #1" to "Failure",
            "Part One -- Validates the examples -- Example #2" to "Success",
            "Part Two -- Validates the examples -- Example #1" to "Failure",
            "Part Two -- Validates the examples -- Example #2" to "Success",
        )
    }
}

@AdventDay(9999, 1, "Sample", "OnlyEdgeCase")
class Y9999D01OnlyEdgeCaseTest : SampleSpec<SampleOnlyEdgeCase>() {
    init {
        testExtension(
            "Part One -- Validates the examples -- Example #1" to "Success",
            "Part One -- Validates the examples -- Example #2" to "Success",
            "Part Two -- Validates the examples -- Example #1" to "Success",
            "Part Two -- Validates the examples -- Example #2" to "Success",
        )
    }
}

@AdventDay(9999, 1, "Sample", "OnlyInput")
class Y9999D01OnlyInputTest : SampleSpec<SampleOnlyInput>() {
    init {
        testExtension(
            "Part One -- Validates the examples -- Example #1" to "Failure",
            "Part One -- Validates the examples -- Example #2" to "Failure",
            "Part Two -- Validates the examples -- Example #1" to "Failure",
            "Part Two -- Validates the examples -- Example #2" to "Failure",
        )
    }
}

@AdventDay(9999, 1, "Sample", "Error")
class Y9999D01ErrorTest : SampleSpec<SampleError>() {
    init {
        testExtension(
            "Part One -- Validates the examples -- Example #1" to "Failure",
            "Part One -- Validates the examples -- Example #2" to "Failure",
            "Part Two -- Validates the examples -- Example #1" to "Failure",
            "Part Two -- Validates the examples -- Example #2" to "Failure",
        )
    }
}
