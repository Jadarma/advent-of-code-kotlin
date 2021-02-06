package aoc.solutions.y2016

import aoc.core.test.AdventTest
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Y2016D03Test : AdventTest(Y2016D03(), 917, 1649) {

    @Test
    fun `Part one examples are valid`() = assertEquals(0, partOne("  5 10  25"))

    @Test
    fun `Part two examples are valid`() = assertEquals(6, partTwo(
        """
        101 301 501
        102 302 502
        103 303 503
        201 401 601
        202 402 602
        203 403 603
        """.trimIndent()
    ))
}
