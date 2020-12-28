package aoc.solutions.y2015

import aoc.core.test.AdventTest
import kotlin.test.Test
import kotlin.test.assertEquals

class Y2015D09Test : AdventTest(Y2015D09(), 117, 909) {

    private val exampleInput =
        """
        London to Dublin = 464
        London to Belfast = 518
        Dublin to Belfast = 141
        """.trimIndent()

    @Test
    fun `Part one examples are valid`() = assertEquals(605, partOne(exampleInput))

    @Test
    fun `Part two examples are valid`() = assertEquals(982, partTwo(exampleInput))
}
