package aoc.solutions.y2015

import aoc.core.test.AdventTest
import kotlin.test.Test
import kotlin.test.assertEquals

class Y2015D20Test : AdventTest(Y2015D20(), 665280, 705600) {

    @Test
    fun `Part one examples are valid`() = assertEquals(8, partOne("130"))
}
