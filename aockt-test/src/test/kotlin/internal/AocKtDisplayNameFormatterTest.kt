package io.github.jadarma.aockt.test.internal

import io.github.jadarma.aockt.core.Solution
import io.github.jadarma.aockt.test.AdventDay
import io.github.jadarma.aockt.test.AdventSpec
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe

class AocKtDisplayNameFormatterTest : FunSpec({

    test("Does not format test cases") {
        AocKtDisplayNameFormatter.format(testCase).shouldBeNull()
    }

    test("Does not format non-advent specs") {
        AocKtDisplayNameFormatter
            .format(AocKtDisplayNameFormatterTest::class)
            .shouldBeNull()
    }

    test("Formats names correctly") {
        mapOf(
            SimpleAdventSpec::class to "Y3000D02",
            TitledAdventSpec::class to "Y3000D02: Custom Title",
            VariantAdventSpec::class to "Y3000D02 (faster)",
            ComplexAdventSpec::class to "Y3000D02: Custom Title (faster)",
        ).forEach { (spec, name) ->
            AocKtDisplayNameFormatter.format(spec) shouldBe name
        }
    }
})

@AdventDay(3000, 2)
private class SimpleAdventSpec : AdventSpec<Solution>()

@AdventDay(3000, 2, title = "Custom Title")
private class TitledAdventSpec : AdventSpec<Solution>()

@AdventDay(3000, 2, variant = "faster")
private class VariantAdventSpec : AdventSpec<Solution>()

@AdventDay(3000, 2, title = "Custom Title", variant = "faster")
private class ComplexAdventSpec : AdventSpec<Solution>()
