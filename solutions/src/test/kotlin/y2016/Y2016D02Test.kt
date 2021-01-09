package aoc.solutions.y2016

import aoc.core.test.AdventTest
import kotlin.test.Test
import kotlin.test.assertEquals

class Y2016D02Test : AdventTest(Y2016D02(), "52981", "74CD2") {

    private val exampleInput =
        """
        ULL
        RRDDD
        LURDL
        UUUUD
        """.trimIndent()

    @Test
    fun `Part one examples are valid`() = assertEquals("1985", partOne(exampleInput))

    @Test
    fun `Part two examples are valid`() = assertEquals("5DB3", partTwo(exampleInput))
}
