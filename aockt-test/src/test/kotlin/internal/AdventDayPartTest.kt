package io.github.jadarma.aockt.test.internal

import io.github.jadarma.aockt.core.Solution
import io.github.jadarma.aockt.test.internal.AdventDayPart.One
import io.github.jadarma.aockt.test.internal.AdventDayPart.Two
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class AdventDayPartTest : FunSpec({

    val testData = PuzzleTestData(
        input = PuzzleInput("input"),
        solutionPartOne = PuzzleAnswer("1:input"),
        solutionPartTwo = PuzzleAnswer("2:input"),
    )

    test("Can extract known answer from test data") {
        testData.solutionToPart(One) shouldBe testData.solutionPartOne
        testData.solutionToPart(Two) shouldBe testData.solutionPartTwo
    }

    test("Can extract part function from a solution") {
        val solution = object : Solution {
            override fun partOne(input: String) = "1:$input"
            override fun partTwo(input: String) = "2:$input"
        }

        with(testData) {
            checkNotNull(input)
            solution.partFunction(One).invoke(input) shouldBe solutionPartOne
            solution.partFunction(Two).invoke(input) shouldBe solutionPartTwo
        }
    }
})
