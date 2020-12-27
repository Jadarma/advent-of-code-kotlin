package aoc.solutions.y2015

import aoc.core.AdventDay

class Y2015D03 : AdventDay(2015, 3, "Perfectly Spherical Houses in a Vacuum") {

    /** Keeps track of Santa's visits. */
    private class SantaTracker {

        private var currentLocation = 0 to 0
        private val _visited = mutableSetOf(currentLocation)

        /** A set of coordinates of houses that this tracker delivered presents to. */
        val visited: Set<Pair<Int, Int>> = _visited

        /** Travels and delivers a present to the house in the given [direction]. */
        fun travel(direction: Char) {
            currentLocation = currentLocation.transpose(direction)
            _visited.add(currentLocation)
        }

        /** Given a coordinate, gets the coordinates of the next point in the given [direction]. */
        private fun Pair<Int, Int>.transpose(direction: Char): Pair<Int, Int> = when (direction) {
            '^' -> first to second + 1
            'v' -> first to second - 1
            '<' -> first - 1 to second
            '>' -> first + 1 to second
            else -> throw IllegalArgumentException("Invalid direction: '$direction'.")
        }
    }

    override fun partOne(input: String): Any =
        SantaTracker().run {
            input.forEach(this::travel)
            visited.count()
        }

    override fun partTwo(input: String): Any {
        val trackers = List(2) { SantaTracker() }
        input.forEachIndexed { index, direction -> trackers[index % 2].travel(direction) }
        return trackers
            .map { it.visited }
            .reduce(Set<*>::plus)
            .count()
    }
}
