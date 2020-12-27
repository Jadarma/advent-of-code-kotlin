package aoc.solutions.y2015

import aoc.core.AdventDay

class Y2015D02 : AdventDay(2015, 2, "I Was Told There Would Be No Math") {

    /** Parses the input and returns a sequence of the three dimensions of each gift, sorted by length. */
    private fun dimensions(input: String) =
        input
            .lineSequence()
            .map { it.split('x').map(String::toInt).sorted() }
            .onEach { require(it.size == 3) { "Invalid input. Expected exactly three integer dimensions." } }
            .map { Triple(it[0], it[1], it[2]) }

    override fun partOne(input: String): Any =
        dimensions(input)
            .map { (l, w, h) -> 2 * (l * w + w * h + h * l) + l * w }
            .sum()

    override fun partTwo(input: String): Any =
        dimensions(input)
            .map { (l, w, h) -> 2 * (l + w) + l * w * h }
            .sum()
}
