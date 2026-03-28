package io.github.jadarma.aockt.test.integration.sample

import io.github.jadarma.aockt.test.AdventDay
import io.github.jadarma.aockt.test.integration.*

@AdventDay(9999, 3, "Sample", "Empty")
class Y9999D03EmptyTest : SampleSpec<SampleEmpty>() {
    init {
        testExtension(
            "Part One -- Validates the examples -- Example #1" to "Failure",
            "Part One -- Validates the examples -- Example #2" to "Failure",
            "Part One -- The solution -- Is correct" to "Failure",
            "Part Two -- Validates the examples -- Example #1" to "Failure",
            "Part Two -- Validates the examples -- Example #2" to "Failure",
            "Part Two -- The solution -- Computes an answer" to "Failure",
        )
    }
}

@AdventDay(9999, 3, "Sample", "Partial")
class Y9999D03PartialTest : SampleSpec<SamplePartial>() {
    init {
        testExtension(
            "Part One -- Validates the examples -- Example #1" to "Success",
            "Part One -- Validates the examples -- Example #2" to "Success",
            "Part One -- The solution -- Is correct" to "Success",
            "Part One -- The solution -- Is reasonably efficient" to "Success",
            "Part Two -- Validates the examples -- Example #1" to "Failure",
            "Part Two -- Validates the examples -- Example #2" to "Failure",
            "Part Two -- The solution -- Computes an answer" to "Failure",
        )
    }
}

@AdventDay(9999, 3, "Sample", "Correct")
class Y9999D03CorrectTest : SampleSpec<SampleCorrect>() {
    init {
        testExtension(
            "Part One -- Validates the examples -- Example #1" to "Success",
            "Part One -- Validates the examples -- Example #2" to "Success",
            "Part One -- The solution -- Is correct" to "Success",
            "Part One -- The solution -- Is reasonably efficient" to "Success",
            "Part Two -- Validates the examples -- Example #1" to "Success",
            "Part Two -- Validates the examples -- Example #2" to "Success",
            "Part Two -- The solution -- Computes an answer" to "Success",
        )
    }
}

@AdventDay(9999, 3, "Sample", "OnlyExample")
class Y9999D03OnlyExampleTest : SampleSpec<SampleOnlyExample>() {
    init {
        testExtension(
            "Part One -- Validates the examples -- Example #1" to "Failure",
            "Part One -- Validates the examples -- Example #2" to "Success",
            "Part One -- The solution -- Is correct" to "Failure",
            "Part Two -- Validates the examples -- Example #1" to "Failure",
            "Part Two -- Validates the examples -- Example #2" to "Success",
            "Part Two -- The solution -- Computes an answer" to "Success",
        )
    }
}

@AdventDay(9999, 3, "Sample", "OnlyEdgeCase")
class Y9999D03OnlyEdgeCaseTest : SampleSpec<SampleOnlyEdgeCase>() {
    init {
        testExtension(
            "Part One -- Validates the examples -- Example #1" to "Success",
            "Part One -- Validates the examples -- Example #2" to "Success",
            "Part One -- The solution -- Is correct" to "Failure",
            "Part Two -- Validates the examples -- Example #1" to "Success",
            "Part Two -- Validates the examples -- Example #2" to "Success",
            "Part Two -- The solution -- Computes an answer" to "Success",
        )
    }
}

@AdventDay(9999, 3, "Sample", "OnlyInput")
class Y9999D03OnlyInputTest : SampleSpec<SampleOnlyInput>() {
    init {
        testExtension(
            "Part One -- Validates the examples -- Example #1" to "Failure",
            "Part One -- Validates the examples -- Example #2" to "Failure",
            "Part One -- The solution -- Is correct" to "Success",
            "Part One -- The solution -- Is reasonably efficient" to "Success",
            "Part Two -- Validates the examples -- Example #1" to "Failure",
            "Part Two -- Validates the examples -- Example #2" to "Failure",
            "Part Two -- The solution -- Computes an answer" to "Success",
        )
    }
}

@AdventDay(9999, 3, "Sample", "Error")
class Y9999D03ErrorTest : SampleSpec<SampleError>() {
    init {
        testExtension(
            "Part One -- Validates the examples -- Example #1" to "Failure",
            "Part One -- Validates the examples -- Example #2" to "Failure",
            "Part One -- The solution -- Is correct" to "Failure",
            "Part Two -- Validates the examples -- Example #1" to "Failure",
            "Part Two -- Validates the examples -- Example #2" to "Failure",
            "Part Two -- The solution -- Computes an answer" to "Failure",
        )
    }
}
