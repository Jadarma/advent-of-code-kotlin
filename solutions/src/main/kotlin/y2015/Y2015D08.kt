package aoc.solutions.y2015

import aoc.core.AdventDay

/**
 * **NOTE:** This solution assumes the input strings are valid (i.e. all escapes are correct, and surrounding quotes
 * are intact). This is, after all, an analysis of Santa's digital list, which is code, and therefore I'll pretend an
 * elf already ran it through a linter before deploying to production.
 */
class Y2015D08 : AdventDay(2015, 8, "Matchsticks") {

    private val escapedHex = Regex("""\\x[0-9a-f]{2}""")
    private val escapedQuote = Regex("""\\"""")
    private val escapedBackslash = Regex("""\\\\""")

    private fun String.memorySize(): Int =
        // The order is important!
        replace(escapedHex, "!")
            .replace(escapedQuote, "!")
            .replace(escapedBackslash, "!")
            .length - 2

    private fun String.escaped(): String =
        replace("""\""", """\\""")
            .replace(""""""", """\"""")
            .let { """"$it"""" }

    override fun partOne(input: String) = input.lineSequence().sumBy { it.length - it.memorySize() }

    override fun partTwo(input: String) = input.lineSequence().sumBy { it.escaped().length - it.length }
}
