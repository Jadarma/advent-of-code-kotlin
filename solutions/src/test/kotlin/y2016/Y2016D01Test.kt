package aoc.solutions.y2016

import aoc.core.test.AdventTest
import kotlin.test.Test
import kotlin.test.assertEquals

class Y2016D01Test : AdventTest(Y2016D01(), 230, 154) {

    @Test
    fun `Part one examples are valid`() {
        assertEquals(5, partOne("R2, L3"))
        assertEquals(2, partOne("R2, R2, R2"))
        assertEquals(12, partOne("R5, L5, R5, R3"))
    }

    @Test
    fun `Part two examples are valid`() {
        assertEquals(4, partTwo("R8, R4, R4, R8"))
    }
}
