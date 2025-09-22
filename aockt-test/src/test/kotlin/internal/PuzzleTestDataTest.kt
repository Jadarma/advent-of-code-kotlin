package io.github.jadarma.aockt.test.internal

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class PuzzleTestDataTest : FunSpec({

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
        test("are inlined") {
            PuzzleAnswer("answer").toString() shouldBe "answer"
        }
    }
})
