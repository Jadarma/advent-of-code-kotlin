package aoc.solutions.y2015

import aoc.core.test.AdventTest
import kotlin.test.Test
import kotlin.test.assertEquals

internal class Y2015D03Test : AdventTest(Y2015D03(), 2592, 2360) {

    @Test
    fun `Part one examples are valid`() {
        assertEquals(2, partOne(">"))
        assertEquals(4, partOne("^>v<"))
        assertEquals(2, partOne("^v^v^v^v^v"))
    }

    @Test
    fun `Part two examples are valid`() {
        assertEquals(3, partTwo("^v"))
        assertEquals(3, partTwo("^>v<"))
        assertEquals(11, partTwo("^v^v^v^v^v"))
    }
}
