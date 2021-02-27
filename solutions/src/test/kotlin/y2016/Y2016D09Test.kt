package aoc.solutions.y2016

import aoc.core.test.AdventTest
import kotlin.test.Test
import kotlin.test.assertEquals

class Y2016D09Test : AdventTest(Y2016D09(), 70186L, 10915059201L) {

    @Test
    fun `Part one examples are valid`() {
        mapOf(
            "ADVENT" to 6L,
            "A(1x5)BC" to 7L,
            "(3x3)XYZ" to 9L,
            "A(2x2)BCD(2x2)EFG" to 11L,
            "(6x1)(1x3)A" to 6L,
            "X(8x2)(3x3)ABCY" to 18L,
        ).forEach { (input, expectedLength) -> assertEquals(expectedLength, partOne(input)) }
    }

    @Test
    fun `Part two examples are valid`() {
        mapOf(
            "(3x3)XYZ" to 9L,
            "X(8x2)(3x3)ABCY" to 20L,
            "(27x12)(20x12)(13x14)(7x10)(1x12)A" to 241920L,
            "(25x3)(3x3)ABC(2x3)XY(5x2)PQRSTX(18x9)(3x2)TWO(5x7)SEVEN" to 445L,
        ).forEach { (input, expectedLength) -> assertEquals(expectedLength, partTwo(input)) }
    }
}
