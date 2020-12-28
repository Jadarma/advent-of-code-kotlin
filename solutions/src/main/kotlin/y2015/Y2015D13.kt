package aoc.solutions.y2015

import aoc.core.AdventDay

class Y2015D13 : AdventDay(2015, 13, "Knights of the Dinner Table") {

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

    private val inputRegex = Regex("""^(\w+) would (lose|gain) (\d+) happiness units by sitting next to (\w+).$""")

    /**
     * Parses a single line of input and returns a triple containing two people and the happiness difference applied if
     * the first person stands next to the second.
     */
    private fun parseInput(input: String): Triple<String, String, Int> {
        val (personA, sign, amount, personB) = inputRegex.matchEntire(input)!!.destructured
        val happiness = amount.toInt() * if (sign == "gain") 1 else -1
        return Triple(personA, personB, happiness)
    }

    /**
     * Given a list of happiness modifiers between all pairs of guests, returns all possible table arrangements and
     * their total happiness modifier.
     * If [includeApatheticSelf], includes an extra neutral person in the list.
     */
    private fun bruteForceArrangement(
        guestData: List<Triple<String, String, Int>>,
        includeApatheticSelf: Boolean = false,
    ): List<Pair<List<String>, Int>> {
        val guests = mutableSetOf<String>()
        val happinessScores = mutableMapOf<String, MutableMap<String, Int>>()

        @Suppress("ReplacePutWithAssignment")
        guestData.forEach { (guest, other, happiness) ->
            guests.add(guest)
            guests.add(other)
            happinessScores
                .getOrPut(guest) { mutableMapOf() }
                .put(other, happiness)
        }

        if (includeApatheticSelf) {
            happinessScores["Self"] = mutableMapOf()
            guests.forEach { guest ->
                happinessScores[guest]!!["Self"] = 0
                happinessScores["Self"]!![guest] = 0
            }
            guests.add("Self")
        }

        return guests
            .toList()
            .permutations()
            .map { arrangement ->
                arrangement to arrangement
                    .plusElement(arrangement.first())
                    .windowed(2)
                    .sumBy { happinessScores[it[0]]!![it[1]]!! + happinessScores[it[1]]!![it[0]]!! }
            }
    }

    override fun partOne(input: String) =
        input
            .lines()
            .map(this::parseInput)
            .let(this::bruteForceArrangement)
            .maxOf { it.second }

    override fun partTwo(input: String) =
        input
            .lines()
            .map(this::parseInput)
            .let { this.bruteForceArrangement(it, includeApatheticSelf = true) }
            .maxOf { it.second }
}
