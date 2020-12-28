package aoc.solutions.y2015

import aoc.core.AdventDay

class Y2015D11 : AdventDay(2015, 11, "Corporate Policy") {

    // Predicates for a valid password
    private val illegalCharacterRule = Regex("""[iol]""")
    private val doublePairRule = Regex("""(.)\1(?!.*\1.*$).*(.)\2""")

    private val passwordValidationRules: List<(String) -> Boolean> = listOf(
        { !it.contains(illegalCharacterRule) },
        { it.contains(doublePairRule) },
        { it.windowed(3).any { c -> c[0] + 1 == c[1] && c[1] + 1 == c[2] } },
    )

    /** Returns the next password by incrementing and handling letter overflows. */
    private fun String.incrementPassword(): String {
        val chars = toCharArray()
        for (i in chars.indices.reversed()) {
            chars[i] = if (chars[i] == 'z') 'a' else chars[i] + 1
            if (chars[i] != 'a') break
        }
        return String(chars)
    }

    /** Generates valid passwords in ascending order starting from the [seed], but excluding it even if valid. */
    private fun passwordSequence(seed: String) =
        generateSequence(seed) { it.incrementPassword() }
            .drop(1)
            .filter { candidate -> passwordValidationRules.all { rule -> rule(candidate) } }

    override fun partOne(input: String) = passwordSequence(input).first()
    override fun partTwo(input: String) = passwordSequence(input).drop(1).first()
}
