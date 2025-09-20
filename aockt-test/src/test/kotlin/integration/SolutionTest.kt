package io.github.jadarma.aockt.test.integration

import io.github.jadarma.aockt.core.Solution
import io.github.jadarma.aockt.test.AdventDay
import io.github.jadarma.aockt.test.AdventSpec
import io.github.jadarma.aockt.test.internal.MissingNoArgConstructorException
import io.github.jadarma.aockt.test.internal.injectSolution
import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

open class SolutionImpl : Solution {
    override fun partOne(input: String) = "1:$input"
    override fun partTwo(input: String) = "2:${input.length}"
}

class ClassSolution : SolutionImpl()
object ObjectSolution : SolutionImpl()
class ConstructedSolution(val arg: Int) : SolutionImpl()

class SolutionTest : FunSpec({

    context("Solution can be instantiated") {
        test("from a simple class") {
            val solution = ClassSolutionSpec::class.injectSolution()
            solution.partOne("A") shouldBe "1:A"
            solution.partTwo("A") shouldBe "2:1"
        }

        test("from an object class") {
            val solution = ObjectSolutionSpec::class.injectSolution()
            solution.partOne("A") shouldBe "1:A"
            solution.partTwo("A") shouldBe "2:1"
        }

        @Suppress("MaxLineLength")
        test("but not from a complex class") {
            shouldThrowExactly<MissingNoArgConstructorException> { ConstructedSolutionSpec::class.injectSolution() }
                .message.shouldBe("Class io.github.jadarma.aockt.test.integration.ConstructedSolution is a Solution but it is missing a no-arg constructor.")
        }
    }
})

@AdventDay(3000, 1)
private class ClassSolutionSpec : AdventSpec<ClassSolution>()

@AdventDay(3000, 4)
private class ObjectSolutionSpec : AdventSpec<ObjectSolution>()

@AdventDay(3000, 4)
private class ConstructedSolutionSpec : AdventSpec<ConstructedSolution>()
