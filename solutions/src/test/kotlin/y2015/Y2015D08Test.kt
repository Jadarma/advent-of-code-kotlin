package aoc.solutions.y2015

import aoc.core.test.AdventTest
import kotlin.test.Test
import kotlin.test.assertEquals

class Y2015D08Test : AdventTest(Y2015D08(), 1371, 2117) {

    private val exampleInput =
        """
        ""
        "abc"
        "aaa\"aaa"
        "\x27"
        """.trimIndent()

    @Test
    fun `Part one examples are valid`() = assertEquals(12, partOne(exampleInput))

    @Test
    fun `Part two examples are valid`() = assertEquals(19, partTwo(exampleInput))
}
