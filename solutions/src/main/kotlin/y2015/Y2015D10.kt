package aoc.solutions.y2015

import aoc.core.AdventDay

class Y2015D10 : AdventDay(2015, 10, "Elves Look, Elves Say") {

    private val repeatingRegex = Regex("""(.)\1*""")

    private fun String.lookAndSay(): String =
        repeatingRegex.replace(this) { it.value.length.toString() + it.value.first() }

    override fun partOne(input: String) = (1..40).fold(input) { acc, _ -> acc.lookAndSay() }.length
    override fun partTwo(input: String) = (1..50).fold(input) { acc, _ -> acc.lookAndSay() }.length
}
