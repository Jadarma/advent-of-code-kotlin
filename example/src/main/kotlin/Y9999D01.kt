import io.github.jadarma.aockt.core.Solution

/** A solution to a fictitious puzzle used for testing. */
class Y9999D01 : Solution {

    private fun parseInput(input: String): List<Int> =
        input
            .splitToSequence(',')
            .map(String::toInt)
            .toList()

    override fun partOne(input: String) = parseInput(input).filter { it % 2 == 1 }.sum()

    override fun partTwo(input: String) = parseInput(input).reduce { a, b -> a * b }
}
