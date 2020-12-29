package aoc.solutions.y2015

import aoc.core.test.AdventTest
import kotlin.test.Test
import kotlin.test.assertEquals

class Y2015D19Test : AdventTest(Y2015D19(), 576, 207) {

    private val exampleInput =
        """
        H => HO
        H => OH
        O => HH
        e => H
        e => O
        
        HOH
        """.trimIndent()

    @Test
    fun `Part one examples are valid`() = assertEquals(4, partOne(exampleInput))

    @Test
    fun `Part two examples are valid`() = assertEquals(3, partTwo(exampleInput))
}
