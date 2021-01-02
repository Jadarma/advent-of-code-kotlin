package aoc.solutions.y2015

import aoc.core.test.AdventTest
import kotlin.test.Test
import kotlin.test.assertEquals

class Y2015D24Test : AdventTest(Y2015D24(), 10723906903L, 74850409L) {

    private val exampleInput =
        """
        1
        2
        3
        4
        5
        7
        8
        9
        10
        11
        """.trimIndent()

    @Test
    fun `Part one examples are valid`() = assertEquals(99L, partOne(exampleInput))

    @Test
    fun `Part two examples are valid`() = assertEquals(44L, partTwo(exampleInput))
}
