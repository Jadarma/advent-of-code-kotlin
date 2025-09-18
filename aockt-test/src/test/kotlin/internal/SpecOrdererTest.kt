package io.github.jadarma.aockt.test.internal

import io.github.jadarma.aockt.core.Solution
import io.github.jadarma.aockt.test.AdventDay
import io.github.jadarma.aockt.test.AdventSpec
import io.kotest.assertions.withClue
import io.kotest.core.spec.SpecRef
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldContainInOrder
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.shuffle
import io.kotest.property.checkAll

class SpecOrdererTest : FunSpec({

    test("Orders specs correctly") {
        val otherSpecs = listOf(OtherA::class, OtherB::class).map(SpecRef::Reference)
        val adventSpecs = listOf(
            SpecA::class, SpecB::class, SpecC::class, SpecD::class,
            SpecE::class, SpecF::class, SpecG::class, SpecH::class,
        ).map(SpecRef::Reference)
        val allSpecs: List<SpecRef> = adventSpecs + otherSpecs

        checkAll(iterations = 128, Arb.shuffle(allSpecs)) { discoveryOrder ->
            val otherOrder = discoveryOrder.filter { it in otherSpecs }
            val sortedOrder = discoveryOrder.sortedWith(SpecOrderer)

            withClue("Sorting changed the relative order of non-AdventSpecs") {
                sortedOrder.shouldContainInOrder(otherOrder)
            }

            withClue("Final sort order is incorrect.") {
                val expectedOrder = otherOrder + adventSpecs
                sortedOrder.shouldBe(expectedOrder)
            }
        }
    }
})

// Some inactive specs to test with.
private class OtherA : FunSpec()
private class OtherB : FunSpec()

@AdventDay(3000, 1)
private class SpecA : AdventSpec<Solution>()

@AdventDay(3000, 1, variant = "default")
private class SpecB : AdventSpec<Solution>()

@AdventDay(3000, 1, title = "A")
private class SpecC : AdventSpec<Solution>()

@AdventDay(3000, 1, title = "A", variant = "custom")
private class SpecD : AdventSpec<Solution>()

@AdventDay(3000, 1, title = "A", variant = "dumb")
private class SpecE : AdventSpec<Solution>()

@AdventDay(3000, 1, title = "B")
private class SpecF : AdventSpec<Solution>()

@AdventDay(3000, 2)
private class SpecG : AdventSpec<Solution>()

@AdventDay(3001, 1)
private class SpecH : AdventSpec<Solution>()
