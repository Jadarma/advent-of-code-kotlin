package aoc.solutions.y2016

import aoc.core.test.AdventTest
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Y2016D06Test : AdventTest(Y2016D06(), "liwvqppc", "caqfbzlh") {

    private val exampleInput = """
        eedadn
        drvtee
        eandsr
        raavrd
        atevrs
        tsrnev
        sdttsa
        rasrtv
        nssdts
        ntnada
        svetve
        tesnvt
        vntsnd
        vrdear
        dvrsen
        enarar
    """.trimIndent()

    @Test
    fun `Part one examples are valid`() = assertEquals("easter", partOne(exampleInput))

    @Test
    fun `Part two examples are valid`() = assertEquals("advent", partTwo(exampleInput))
}
