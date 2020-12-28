package aoc.solutions.y2015

import aoc.core.test.AdventTest
import kotlin.test.Test
import kotlin.test.assertEquals

class Y2015D11Test : AdventTest(Y2015D11(), "hepxxyzz", "heqaabcc") {

    @Test
    fun `Part one examples are valid`() {
        assertEquals("abcdffaa", partOne("abcdefgh"))
        assertEquals("ghjaabcc", partOne("ghijklmn"))
    }
}
