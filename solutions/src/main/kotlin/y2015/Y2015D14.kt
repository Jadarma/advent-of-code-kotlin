package aoc.solutions.y2015

import aoc.core.AdventDay

class Y2015D14 : AdventDay(2015, 14, "Reindeer Olympics") {

    private data class Reindeer(val name: String, val speed: Int, val sprint: Int, val rest: Int)

    private val inputRegex =
        Regex("""(\w+) can fly (\d+) km/s for (\d+) seconds, but then must rest for (\d+) seconds\.""")

    /** Parses a single line of input and returns a [Reindeer] and its statistics. */
    private fun parseInput(input: String): Reindeer {
        val (name, speed, sprint, rest) = inputRegex.matchEntire(input)!!.destructured
        return Reindeer(name, speed.toInt(), sprint.toInt(), rest.toInt())
    }

    /** Calculates the total distance this [Reindeer] travelled after exactly this many [seconds]. */
    private fun Reindeer.distanceAfter(seconds: Int): Int {
        val cycle = sprint + rest
        val completeCycles = seconds / cycle
        val moduloSprint = minOf(sprint, seconds % cycle)
        return (completeCycles * sprint + moduloSprint) * speed
    }

    /** Races the [contestants], returning their associated scores after each increment (in seconds). */
    private fun reindeerRace(contestants: List<Reindeer>) = sequence<Map<Reindeer, Int>> {
        val currentScore = contestants.associateWithTo(mutableMapOf()) { 0 }
        var seconds = 0
        yield(currentScore)

        while (true) {
            seconds += 1
            val reindeerDistances = contestants.associateWith { it.distanceAfter(seconds) }
            val maxDistance = reindeerDistances.maxOf { it.value }

            reindeerDistances
                .filter { it.value == maxDistance }
                .forEach { (reindeer, _) -> currentScore[reindeer] = currentScore[reindeer]!! + 1 }

            yield(currentScore)
        }
    }

    override fun partOne(input: String) =
        input
            .lineSequence()
            .map(this::parseInput)
            .map { it.distanceAfter(seconds = 2503) }
            .maxOrNull()!!

    override fun partTwo(input: String): Int {
        val contestants = input.lines().map(this::parseInput)
        return reindeerRace(contestants).elementAt(2503).maxOf { it.value }
    }
}
