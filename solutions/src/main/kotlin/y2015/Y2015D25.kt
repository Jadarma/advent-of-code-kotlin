package aoc.solutions.y2015

import aoc.core.AdventDay

class Y2015D25 : AdventDay(2015, 25, "Let it Snow") {

    private val inputRegex =
        Regex("""^To continue, please consult the code grid in the manual\.\s+Enter the code at row (\d+), column (\d+)\.$""")

    /** Parses the input and returns the code's coordinates. */
    private fun parseInput(input: String): Pair<Int, Int> {
        val (x, y) = inputRegex.matchEntire(input)!!.destructured
        return x.toInt() to y.toInt()
    }

    /** Returns the activation code for the machine given its [point] coordinates. */
    private fun codeFor(point: Pair<Int, Int>): Long {
        val (x, y) = point
        require(x > 0 && y > 0) { "Invalid coordinates." }
        val iterations = (x + y - 1) * (x + y) / 2 - x + 1
        return (1 until iterations).fold(20151125L) { acc, _ -> (acc * 252533L) % 33554393L }
    }

    override fun partOne(input: String) = parseInput(input).let(this::codeFor)
    override fun partTwo(input: String) = throw NotImplementedError("There is no part two for $this.")
}
