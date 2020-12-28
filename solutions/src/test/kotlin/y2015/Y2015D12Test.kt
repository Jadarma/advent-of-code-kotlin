package aoc.solutions.y2015

import aoc.core.test.AdventTest
import kotlin.test.Test
import kotlin.test.assertEquals

class Y2015D12Test : AdventTest(Y2015D12(), 119433, 68466) {

    @Test
    fun `Part one examples are valid`() {
        assertEquals(6, partOne("""[1,2,3]"""))
        assertEquals(6, partOne("""{"a":2,"b":4}"""))
        assertEquals(3, partOne("""[[[3]]]"""))
        assertEquals(3, partOne("""{"a":{"b":4},"c":-1}"""))
        assertEquals(0, partOne("""{"a":[-1,1]}"""))
        assertEquals(0, partOne("""[-1,{"a":1}]"""))
        assertEquals(0, partOne("""[]"""))
        assertEquals(0, partOne("""{}"""))
    }

    @Test
    fun `Part two examples are valid`() {
        assertEquals(6, partTwo("""[1,2,3]"""))
        assertEquals(4, partTwo("""[1,{"c":"red","b":2},3]"""))
        assertEquals(0, partTwo("""{"d":"red","e":[1,2,3,4],"f":5}"""))
        assertEquals(6, partTwo("""[1,"red",5]"""))
    }
}
