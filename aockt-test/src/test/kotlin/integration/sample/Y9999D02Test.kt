package io.github.jadarma.aockt.test.integration.sample

import io.github.jadarma.aockt.test.AdventDay
import io.github.jadarma.aockt.test.integration.*

@AdventDay(9999, 2, "Sample", "Empty")
class Y9999D02EmptyTest : SampleSpec<SampleEmpty>() {
    init {
        testExtension(
            "Part One -- Validates the examples -- Example #1" to "Failure",
            "Part One -- Validates the examples -- Example #2" to "Failure",
            "Part One -- The solution -- Computes an answer" to "Failure",
            "Part Two -- Validates the examples -- Example #1" to "Failure",
            "Part Two -- Validates the examples -- Example #2" to "Failure",
            "Part Two -- The solution -- Computes an answer" to "Failure",
        )
    }
}

@AdventDay(9999, 2, "Sample", "Partial")
class Y9999D02PartialTest : SampleSpec<SamplePartial>() {
    init {
        testExtension(
            "Part One -- Validates the examples -- Example #1" to "Success",
            "Part One -- Validates the examples -- Example #2" to "Success",
            "Part One -- The solution -- Computes an answer" to "Success",
            "Part Two -- Validates the examples -- Example #1" to "Failure",
            "Part Two -- Validates the examples -- Example #2" to "Failure",
            "Part Two -- The solution -- Computes an answer" to "Failure",
        )
    }
}

@AdventDay(9999, 2, "Sample", "Correct")
class Y9999D02CorrectTest : SampleSpec<SampleCorrect>() {
    init {
        testExtension(
            "Part One -- Validates the examples -- Example #1" to "Success",
            "Part One -- Validates the examples -- Example #2" to "Success",
            "Part One -- The solution -- Computes an answer" to "Success",
            "Part Two -- Validates the examples -- Example #1" to "Success",
            "Part Two -- Validates the examples -- Example #2" to "Success",
            "Part Two -- The solution -- Computes an answer" to "Success",
        )
    }
}

@AdventDay(9999, 2, "Sample", "OnlyExample")
class Y9999D02OnlyExampleTest : SampleSpec<SampleOnlyExample>() {
    init {
        testExtension(
            "Part One -- Validates the examples -- Example #1" to "Failure",
            "Part One -- Validates the examples -- Example #2" to "Success",
            "Part One -- The solution -- Computes an answer" to "Success",
            "Part Two -- Validates the examples -- Example #1" to "Failure",
            "Part Two -- Validates the examples -- Example #2" to "Success",
            "Part Two -- The solution -- Computes an answer" to "Success",
        )
    }
}

@AdventDay(9999, 2, "Sample", "OnlyEdgeCase")
class Y9999D02OnlyEdgeCaseTest : SampleSpec<SampleOnlyEdgeCase>() {
    init {
        testExtension(
            "Part One -- Validates the examples -- Example #1" to "Success",
            "Part One -- Validates the examples -- Example #2" to "Success",
            "Part One -- The solution -- Computes an answer" to "Success",
            "Part Two -- Validates the examples -- Example #1" to "Success",
            "Part Two -- Validates the examples -- Example #2" to "Success",
            "Part Two -- The solution -- Computes an answer" to "Success",
        )
    }
}

@AdventDay(9999, 2, "Sample", "OnlyInput")
class Y9999D02OnlyInputTest : SampleSpec<SampleOnlyInput>() {
    init {
        testExtension(
            "Part One -- Validates the examples -- Example #1" to "Failure",
            "Part One -- Validates the examples -- Example #2" to "Failure",
            "Part One -- The solution -- Computes an answer" to "Success",
            "Part Two -- Validates the examples -- Example #1" to "Failure",
            "Part Two -- Validates the examples -- Example #2" to "Failure",
            "Part Two -- The solution -- Computes an answer" to "Success",
        )
    }
}

@AdventDay(9999, 2, "Sample", "Error")
class Y9999D02ErrorTest : SampleSpec<SampleError>() {
    init {
        testExtension(
            "Part One -- Validates the examples -- Example #1" to "Failure",
            "Part One -- Validates the examples -- Example #2" to "Failure",
            "Part One -- The solution -- Computes an answer" to "Failure",
            "Part Two -- Validates the examples -- Example #1" to "Failure",
            "Part Two -- Validates the examples -- Example #2" to "Failure",
            "Part Two -- The solution -- Computes an answer" to "Failure",
        )
    }
}
