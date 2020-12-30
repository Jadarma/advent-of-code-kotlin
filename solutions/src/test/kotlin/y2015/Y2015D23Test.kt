package aoc.solutions.y2015

import aoc.core.test.AdventTest
import kotlin.test.Test
import kotlin.test.assertEquals

@ExperimentalUnsignedTypes
class Y2015D23Test : AdventTest(Y2015D23(), 170.toUInt(), 247.toUInt()) {

    private val exampleInput =
        """
        inc b
        jio b, +2
        tpl b
        inc b
        """.trimIndent()

    @Test
    fun `Part one examples are valid`() = assertEquals(2.toUInt(), partOne(exampleInput))
}
