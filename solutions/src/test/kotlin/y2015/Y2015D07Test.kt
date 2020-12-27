package aoc.solutions.y2015

import aoc.core.test.AdventTest
import kotlin.test.Test
import kotlin.test.assertEquals

@ExperimentalUnsignedTypes
class Y2015D07Test : AdventTest(Y2015D07(), 956.toUShort(), 40149.toUShort()) {

    @Test
    fun `Part one examples are valid`() {
        val input =
            """
            123 -> x
            456 -> y
            x AND y -> d
            x OR y -> e
            x LSHIFT 2 -> f
            y RSHIFT 2 -> g
            NOT x -> h
            NOT y -> a
            """.trimIndent()
        assertEquals(65079.toUShort(), partOne(input))
    }
}
