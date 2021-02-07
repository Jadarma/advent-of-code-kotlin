package aoc.solutions.y2016

import aoc.core.test.AdventTest
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Y2016D05Test : AdventTest(Y2016D05(), "4543c154", "1050cbbd") {

    @Test
    fun `Part one examples are valid`() = assertEquals("18f47a30", partOne("abc"))

    @Test
    fun `Part two examples are valid`() = assertEquals("05ace8e3", partTwo("abc"))
}
