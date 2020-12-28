package aoc.solutions.y2015

import aoc.core.AdventDay

class Y2015D09 : AdventDay(2015, 9, "All in a Single Night") {

    // The code for generating permutations is a slightly refactored version of Marcin Moskala's implementation,
    // because it's short and I want to keep dependencies to a minimum. Find the original at:
    // https://github.com/MarcinMoskala/KotlinDiscreteMathToolkit/blob/17a7329af042c5de232051027ec1155011d57da8/src/main/java/com/marcinmoskala/math/PermutationsExt.kt#L20
    private fun <T> List<T>.permutations(): Set<List<T>> = when (size) {
        0 -> emptySet()
        1 -> setOf(take(1))
        else -> drop(1)
            .permutations()
            .flatMap { sublist -> (0..sublist.size).map { sublist.plusAt(it, first()) } }
            .toSet()
    }

    private fun <T> List<T>.plusAt(index: Int, element: T): List<T> = when (index) {
        !in 0..size -> throw Error("Cannot put at index $index because size is $size")
        0 -> listOf(element) + this
        size -> this + element
        else -> dropLast(size - index) + element + drop(index)
    }

    private val inputRegex = Regex("""^(\w+) to (\w+) = (\d+)$""")

    /** Parses a single line of input and returns a triple containing two locations and the distance between them. */
    private fun parseInput(input: String): Triple<String, String, Int> {
        val (from, to, distance) = inputRegex.matchEntire(input)!!.destructured
        return Triple(from, to, distance.toInt())
    }

    /**
     * Given a list of distances between all pairs of locations on the map, returns all possible paths that visit all
     * of them and their total distance.
     */
    private fun bruteForceRoutes(locationData: List<Triple<String, String, Int>>): List<Pair<List<String>, Int>> {
        val locations = mutableSetOf<String>()
        val distances = mutableMapOf<Pair<String, String>, Int>()

        locationData.forEach { (from, to, distance) ->
            locations.add(from)
            locations.add(to)
            distances[from to to] = distance
            distances[to to from] = distance
        }

        return locations
            .toList()
            .permutations()
            .map { route -> route to route.windowed(2).sumBy { distances.getValue(it[0] to it[1]) } }
    }

    override fun partOne(input: String) =
        input
            .lines()
            .map(this::parseInput)
            .let(this::bruteForceRoutes)
            .minOf { it.second }

    override fun partTwo(input: String) =
        input
            .lines()
            .map(this::parseInput)
            .let(this::bruteForceRoutes)
            .maxOf { it.second }
}
