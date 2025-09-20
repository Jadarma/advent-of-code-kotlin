package io.github.jadarma.aockt.test.internal

import io.github.jadarma.aockt.test.AdventDay
import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.assertions.withClue
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.shuffle
import io.kotest.property.checkAll

class AdventDayIDTest : FunSpec({

    test("Input is validated") {
        for (invalidYear in listOf(-2, 1, 2008, 2014, 10_000, 99_999)) {
            withClue("Did not reject invalid year: $invalidYear") {
                shouldThrowExactly<IllegalArgumentException> {
                    AdventDayID(invalidYear, 1)
                }
            }
        }

        for (invalidDay in listOf(-1, 0, 26, 32)) {
            withClue("Did not reject invalid day: $invalidDay") {
                shouldThrowExactly<IllegalArgumentException> {
                    AdventDayID(2015, invalidDay)
                }
            }
        }
    }

    test("Is properly formatted") {
        AdventDayID(2015, 1).toString() shouldBe "Y2015D01"
        AdventDayID(2345, 16).toString() shouldBe "Y2345D16"
    }

    test("Is chronologically ordered") {

        val y2015d25 = AdventDayID(2015, 25)
        val y2016d02 = AdventDayID(2016, 2)
        val y2016d04 = AdventDayID(2016, 4)
        val y2017d01 = AdventDayID(2017, 1)

        val days = listOf(y2015d25, y2016d02, y2016d04, y2017d01)

        withClue("Not sorted correctly") {
            checkAll(iterations = 10, Arb.shuffle(days)) { shuffled ->
                shuffled.sorted() shouldBe days
            }
        }
    }

    test("Can be derived from AdventDay annotation") {
        val annotation = AdventDay(2015, 25)
        annotation.id shouldBe AdventDayID(2015, 25)
    }
})
