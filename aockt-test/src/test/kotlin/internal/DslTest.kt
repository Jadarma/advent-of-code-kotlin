package io.github.jadarma.aockt.test.internal

import io.github.jadarma.aockt.test.AdventDay
import io.github.jadarma.aockt.test.AdventSpec
import io.github.jadarma.aockt.test.ExecMode
import io.github.jadarma.aockt.test.integration.ObjectSolution
import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import kotlin.time.Duration.Companion.seconds

class DslTest : FunSpec({

    context("Cannot register duplicate") {
        val scope = AdventRootScopeImpl(SomeSpec::class).apply {
            partOne()
            partTwo()
            debug {}
        }
        test("partOne scopes") {
            shouldThrowExactly<DuplicateDefinitionException> { scope.partOne() }
                .message
                .shouldBe("In io.github.jadarma.aockt.test.internal.SomeSpec, partOne has been declared twice.")
        }
        test("partTwo scopes") {
            shouldThrowExactly<DuplicateDefinitionException> { scope.partTwo() }
                .message
                .shouldBe("In io.github.jadarma.aockt.test.internal.SomeSpec, partTwo has been declared twice.")
        }
        test("debug scopes") {
            shouldThrowExactly<DuplicateDefinitionException> { scope.debug {} }
                .message
                .shouldBe("In io.github.jadarma.aockt.test.internal.SomeSpec, debug has been declared twice.")
        }
    }

    context("Builds correct configuration") {
        val scope = AdventRootScopeImpl(SomeSpec::class)

        test("For default part entry") {
            scope.partOne.shouldBeNull()
            scope.partOne()
            scope.partOne.shouldNotBeNull().should { config ->
                config.part shouldBe AdventDayPart.One
                config.partFunction.invoke(PuzzleInput("A")).toString() shouldBe "1:A"
                config.enabled.shouldBeTrue()
                config.expensive.shouldBeFalse()
                config.executionMode.shouldBeNull()
                config.efficiencyBenchmark.shouldBeNull()
                config.examples.shouldBeEmpty()
            }
        }

        test("For complex part entry") {
            scope.partTwo.shouldBeNull()
            scope.partTwo(
                enabled = false,
                executionMode = ExecMode.SkipExamples,
                efficiencyBenchmark = 5.seconds,
                expensive = true,
            ) {
                "ABC" shouldOutput "2:3"
                listOf("A", "B", "C") shouldAllOutput "2:1"
            }

            scope.partTwo.shouldNotBeNull().should { config ->
                config.part shouldBe AdventDayPart.Two
                config.partFunction.invoke(PuzzleInput("ABC")).toString() shouldBe "2:3"
                config.enabled.shouldBeFalse()
                config.expensive.shouldBeTrue()
                config.executionMode shouldBe ExecMode.SkipExamples
                config.efficiencyBenchmark shouldBe 5.seconds
                config.examples shouldBe listOf(
                    PuzzleInput("ABC") to PuzzleAnswer("2:3"),
                    PuzzleInput("A") to PuzzleAnswer("2:1"),
                    PuzzleInput("B") to PuzzleAnswer("2:1"),
                    PuzzleInput("C") to PuzzleAnswer("2:1"),
                )
            }
        }

        test("For debug entry.") {
            var wasInvoked = false

            scope.debug.shouldBeNull()
            scope.debug {
                wasInvoked = true
                solution shouldBe ObjectSolution
                input shouldBe "B"
            }

            scope.debug.shouldNotBeNull().should { config ->
                wasInvoked.shouldBeFalse()
                config.test.invoke(AdventDebugScopeImpl(config.solution, PuzzleInput("B")))
                wasInvoked.shouldBeTrue()
            }
        }
    }

    context("Configurations compute final test configs") {
        val configA = AdventTestConfig(
            id = AdventDayID(9999, 2),
            part = AdventDayPart.One,
            partFunction = ObjectSolution.partFunction(AdventDayPart.One),
            enabled = true,
            expensive = true,
            executionMode = ExecMode.SkipExamples,
            efficiencyBenchmark = 10.seconds,
            examples = listOf(PuzzleInput("B") to PuzzleAnswer("1:B"))
        )
        val configB = AdventTestConfig(
            id = AdventDayID(9999, 2),
            part = AdventDayPart.Two,
            partFunction = ObjectSolution.partFunction(AdventDayPart.Two),
            enabled = false,
            expensive = false,
            executionMode = null,
            efficiencyBenchmark = null,
            examples = emptyList(),
        )

        test("for examples") {
            configA.forExamples(AdventProjectConfig.Default).should { finalConfig ->
                finalConfig.enabled shouldBe false
                finalConfig.examples shouldBe configA.examples
                finalConfig.partFunction.invoke(PuzzleInput("B")) shouldBe PuzzleAnswer("1:B")
            }
            configB.forExamples(AdventProjectConfig.Default).should { finalConfig ->
                finalConfig.enabled shouldBe false
                finalConfig.examples shouldBe emptyList()
                finalConfig.partFunction.invoke(PuzzleInput("B")) shouldBe PuzzleAnswer("2:1")
            }
        }

        test("for input") {
            configA.forInput(AdventProjectConfig.Default).should { finalConfig ->
                finalConfig.id shouldBe configA.id
                finalConfig.part shouldBe configA.part
                finalConfig.enabled shouldBe true
                finalConfig.partFunction.invoke(PuzzleInput("B")) shouldBe PuzzleAnswer("1:B")
                finalConfig.efficiencyBenchmark shouldBe configA.efficiencyBenchmark
            }
            configB.forInput(AdventProjectConfig.Default).should { finalConfig ->
                finalConfig.id shouldBe configB.id
                finalConfig.part shouldBe configB.part
                finalConfig.enabled shouldBe false
                finalConfig.partFunction.invoke(PuzzleInput("B")) shouldBe PuzzleAnswer("2:1")
                finalConfig.efficiencyBenchmark shouldBe AdventProjectConfig.Default.efficiencyBenchmark
            }
        }
    }
})

@AdventDay(3000, 3)
private class SomeSpec : AdventSpec<ObjectSolution>()
