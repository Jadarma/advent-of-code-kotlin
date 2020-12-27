package aoc.solutions.y2015

import aoc.core.test.AdventTest
import kotlin.test.Test
import kotlin.test.assertEquals

class Y2015D05Test : AdventTest(Y2015D05(), 255, 55) {

    @Test
    fun `Part one examples are valid`() {
        assertEquals(1, partOne("ugknbfddgicrmopn"))
        assertEquals(1, partOne("aaa"))
        assertEquals(0, partOne("jchzalrnumimnmhp"))
        assertEquals(0, partOne("haegwjzuvuyypxyu"))
        assertEquals(0, partOne("dvszwmarrgswjxmb"))
    }

    @Test
    fun `Part two examples are valid`() {
        assertEquals(1, partTwo("qjhvhtzxzqqjkmpb"))
        assertEquals(1, partTwo("xxyxx"))
        assertEquals(0, partTwo("uurcxstgmygtbstg"))
        assertEquals(0, partTwo("ieodomkazucvgmuy"))
    }
}
