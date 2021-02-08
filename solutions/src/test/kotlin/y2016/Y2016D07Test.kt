package aoc.solutions.y2016

import aoc.core.test.AdventTest
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

@ExperimentalStdlibApi
class Y2016D07Test : AdventTest(Y2016D07(), 118, 260) {

    @Test
    fun `Part one examples are valid`() = assertEquals(2, partOne(
        """
        abba[mnop]qrst
        abcd[bddb]xyyx
        aaaa[qwer]tyui
        ioxxoj[asdfgh]zxcvbn
        """.trimIndent()
    ))

    @Test
    fun `Part two examples are valid`() = assertEquals(3, partTwo(
        """
        aba[bab]xyz
        xyx[xyx]xyx
        aaa[kek]eke
        zazbz[bzb]cdb
        """.trimIndent()
    ))
}
