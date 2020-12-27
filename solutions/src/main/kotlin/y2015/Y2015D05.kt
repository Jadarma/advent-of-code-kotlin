package aoc.solutions.y2015

import aoc.core.AdventDay

class Y2015D05 : AdventDay(2015, 5, "Doesn't He Have Intern-Elves For This?") {

    // Validate this first so you can use a simple '.' for any character instead of [a-z] everywhere.
    // Technically not needed since input is clean, but we should be thorough ;)
    private val onlyLetters = Regex("""^[a-z]*$""")

    // Rules for Part One
    private val atLeastThreeVowels = Regex("""([aeiou].*){3,}""")
    private val doubledLetter = Regex("""(.)\1""")
    private val containsNaughtyString = Regex("""ab|cd|pq|xy""")

    // Rules for Part Two
    private val doublePairs = Regex("""(..).*\1""")
    private val sandwichedLetter = Regex("""(.).\1""")

    override fun partOne(input: String) =
        input
            .lineSequence()
            .filter { onlyLetters.matches(it) }
            .filter { atLeastThreeVowels.containsMatchIn(it) }
            .filter { doubledLetter.containsMatchIn(it) }
            .filterNot { containsNaughtyString.containsMatchIn(it) }
            .count()

    override fun partTwo(input: String) =
        input
            .lineSequence()
            .filter { onlyLetters.matches(it) }
            .filter { doublePairs.containsMatchIn(it) }
            .filter { sandwichedLetter.containsMatchIn(it) }
            .count()
}
