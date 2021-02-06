package aoc.solutions.y2016

import aoc.core.AdventDay

class Y2016D03 : AdventDay(2016, 3, "Squares With Three Sides") {

    /** Parses the input, returning the number rows as triples of Ints. */
    private fun parseInput(input: String): Sequence<Triple<Int, Int, Int>> =
        input
            .lineSequence()
            .map {
                val (a, b, c) = it
                    .trim()
                    .replace(Regex("""\s+"""), ",")
                    .split(',')
                    .map(String::toInt)
                Triple(a, b, c)
            }

    /** Given a sequence of rows of three ints, maps it to a sequence of vertically transposed numbers. */
    private fun Sequence<Triple<Int, Int, Int>>.readVertically(): Sequence<Triple<Int, Int, Int>> =
        chunked(3) { row ->
            sequenceOf(
                Triple(row[0].first, row[1].first, row[2].first),
                Triple(row[0].second, row[1].second, row[2].second),
                Triple(row[0].third, row[1].third, row[2].third),
            )
        }.flatten()

    /** Checks whether three integers could be the values of the length of a triangle. */
    private fun Triple<Int, Int, Int>.couldBeTriangle(): Boolean = toList().sorted().let { (a, b, c) -> a + b > c }

    override fun partOne(input: String) = parseInput(input).count { it.couldBeTriangle() }
    override fun partTwo(input: String) = parseInput(input).readVertically().count { it.couldBeTriangle() }
}
