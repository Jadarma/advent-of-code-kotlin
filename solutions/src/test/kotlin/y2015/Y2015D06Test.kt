package aoc.solutions.y2015

import aoc.core.test.AdventTest
import kotlin.test.Test
import kotlin.test.assertEquals

class Y2015D06Test : AdventTest(Y2015D06(), 543903, 14687245) {

    @Test
    fun `Part one examples are valid`() {
        val input =
            """
            turn on 0,0 through 999,999
            toggle 0,0 through 999,0
            turn off 499,499 through 500,500
            """.trimIndent()
        assertEquals(1_000_000 - 1000 - 4, partOne(input))
    }

    @Test
    fun `Part two examples are valid`() {
        val input =
            """
            turn on 0,0 through 0,0
            toggle 0,0 through 999,999
            """.trimIndent()
        assertEquals(2_000_001, partTwo(input))
    }
}
