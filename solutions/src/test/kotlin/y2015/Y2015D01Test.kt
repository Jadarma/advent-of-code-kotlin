package aoc.solutions.y2015

import aoc.core.test.AdventTest
import kotlin.test.Test
import kotlin.test.assertEquals

internal class Y2015D01Test : AdventTest(Y2015D01(), 232, 1783) {

    @Test
    fun `Part one examples are valid`() {
        assertEquals(0, partOne("(())"))
        assertEquals(0, partOne("()()"))
        assertEquals(3, partOne("((("))
        assertEquals(3, partOne("(()(()("))
        assertEquals(3, partOne("))((((("))
        assertEquals(-1, partOne("())"))
        assertEquals(-1, partOne("))("))
        assertEquals(-3, partOne(")))"))
        assertEquals(-3, partOne(")())())"))
    }

    @Test
    fun `Part two examples are valid`() {
        assertEquals(1, partTwo(")"))
        assertEquals(5, partTwo("()())"))
    }
}
