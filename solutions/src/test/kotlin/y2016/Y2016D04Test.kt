package aoc.solutions.y2016

import aoc.core.test.AdventTest
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Y2016D04Test : AdventTest(Y2016D04(), 173787, 548) {

    @Test
    fun `Part one examples are valid`() = assertEquals(1514, partOne(
        """
        aaaaa-bbb-z-y-x-123[abxyz]
        a-b-c-d-e-f-g-h-987[abcde]
        not-a-real-room-404[oarel]
        totally-real-room-200[decoy]
        """.trimIndent()
    ))
}
