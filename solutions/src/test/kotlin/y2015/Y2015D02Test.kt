package aoc.solutions.y2015

import aoc.core.test.AdventTest
import kotlin.test.Test
import kotlin.test.assertEquals

internal class Y2015D02Test : AdventTest(Y2015D02(), 1606483, 3842356) {

    @Test
    fun `Part one examples are valid`() {
        assertEquals(58, partOne("2x3x4"))
        assertEquals(43, partOne("1x1x10"))
    }

    @Test
    fun `Part two examples are valid`() {
        assertEquals(34, partTwo("2x3x4"))
        assertEquals(14, partTwo("1x1x10"))
    }
}
