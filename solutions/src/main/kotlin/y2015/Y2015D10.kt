package aoc.solutions.y2015

import aoc.core.AdventDay

class Y2015D10 : AdventDay(2015, 10, "Elves Look, Elves Say") {

    private val repeatingRegex = Regex("""(.)\1*""")

    /** Plays the look and say game, yielding the next description in the sequence. */
    private fun lookAndSay(seed: String) = generateSequence(seed) { current ->
        current.replace(repeatingRegex) { it.value.length.toString() + it.value.first() }
    }

    override fun partOne(input: String) = lookAndSay(input).elementAt(40).length
    override fun partTwo(input: String) = lookAndSay(input).elementAt(50).length
}
