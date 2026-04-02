package io.github.jadarma.aockt.internal

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe

class TestDataTest : FunSpec({

    context("Puzzle inputs") {
        test("cannot be empty") {
            shouldThrow<IllegalArgumentException> { PuzzleInput("") }
        }

        test("are inlined") {
            PuzzleInput("aaa").toString() shouldBe "aaa"
        }

        @Suppress("StringShouldBeRawString")
        test("can be previewed") {
            val singleLine = PuzzleInput("A")
            val multiLine = PuzzleInput("B\n1\n2\nB")
            val manyLines = PuzzleInput("C\n1\n2\n3\n4\n5\n6\nC")

            singleLine.preview() shouldBe "A"
            multiLine.preview() shouldBe "\nB\n1\n2\nB\n"
            manyLines.preview() shouldBe "\nC\n1\n2\n...\nC\n"
        }
    }

    context("Puzzle answers") {
        test("cannot be empty") {
            shouldThrow<IllegalArgumentException> { PuzzleAnswer("") }
        }

        test("are inlined") {
            PuzzleAnswer("answer").toString() shouldBe "answer"
        }
    }

    context("Puzzle test data") {
        test("can read empty days") {
            val data = TestData.inputFor(AdventDayID(9999, 1))
            data.input.shouldBeNull()
            data.solutionPartOne.shouldBeNull()
            data.solutionPartTwo.shouldBeNull()
        }

        test("can read days with only input") {
            val data = TestData.inputFor(AdventDayID(9999, 2))
            data.input.shouldNotBeNull().toString() shouldBe "ABC"
            data.solutionPartOne.shouldBeNull()
            data.solutionPartTwo.shouldBeNull()
        }

        test("can read days with partial solves input") {
            val data = TestData.inputFor(AdventDayID(9999, 3))
            data.input.shouldNotBeNull().toString() shouldBe "ABC"
            data.solutionPartOne.shouldNotBeNull().toString() shouldBe "1:ABC"
            data.solutionPartTwo.shouldBeNull()
        }

        test("can read days with all data") {
            val data = TestData.inputFor(AdventDayID(9999, 4))
            data.input.shouldNotBeNull().toString() shouldBe "ABC"
            data.solutionPartOne.shouldNotBeNull().toString() shouldBe "1:ABC"
            data.solutionPartTwo.shouldNotBeNull().toString() shouldBe "2:3"
        }
    }
})
