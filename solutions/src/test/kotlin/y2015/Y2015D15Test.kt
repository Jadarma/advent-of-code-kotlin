package aoc.solutions.y2015

import aoc.core.test.AdventTest
import kotlin.test.Test
import kotlin.test.assertEquals

class Y2015D15Test : AdventTest(Y2015D15(), 13882464, 11171160) {

    private val exampleInput =
        """
        Butterscotch: capacity -1, durability -2, flavor 6, texture 3, calories 8
        Cinnamon: capacity 2, durability 3, flavor -2, texture -1, calories 3
        """.trimIndent()

    @Test
    fun `Part one examples are valid`() = assertEquals(62842880, partOne(exampleInput))

    @Test
    fun `Part two examples are valid`() = assertEquals(57600000, partTwo(exampleInput))
}
