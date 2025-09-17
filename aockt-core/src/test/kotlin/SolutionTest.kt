package io.github.jadarma.aockt.core

import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf

class SolutionTest : FunSpec({

    test("Has default implementations.") {
        val stub = object : Solution {}
        shouldThrowExactly<NotImplementedError> { stub.partOne("foo") }
            .message shouldBe "Part 1 not implemented."
        shouldThrowExactly<NotImplementedError> { stub.partTwo("bar") }
            .message shouldBe "Part 2 not implemented."
    }

    test("Passes example implementation.") {
        val incrementer = object : Solution {
            override fun partOne(input: String): Any = input.toInt().inc()
            override fun partTwo(input: String): Any = input.toInt().dec()
        }

        incrementer.partOne("1").shouldBeInstanceOf<Int>().shouldBe(2)
        incrementer.partTwo("1").shouldBeInstanceOf<Int>().shouldBe(0)
    }
})
