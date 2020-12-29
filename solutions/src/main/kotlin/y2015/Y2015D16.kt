package aoc.solutions.y2015

import aoc.core.AdventDay

class Y2015D16 : AdventDay(2015, 16, "Aunt Sue") {

    private val propertyRegex = Regex("""([a-z]+): (\d+)""")
    private val sueIdRegex = Regex("""^Sue (\d+):.*$""")

    /** Parses a single line of input and returns what information you know about any [AuntSue]. */
    private fun parseInput(input: String) = AuntSue(
        id = sueIdRegex.matchEntire(input)!!.groups[1]!!.value.toInt(),
        properties = propertyRegex.findAll(input).associate {
            val (name, value) = it.destructured
            name to value.toInt()
        }
    )

    /** All you know about one of your many aunts. */
    private data class AuntSue(val id: Int, val properties: Map<String, Int>)

    /**
     * Tests these properties against the [mfcsamRules] and returns whether they meet all criteria.
     * If there is a rule for a key that is not present in this map, it is ignored.
     */
    private fun Map<String, Int>.consistentWith(mfcsamRules: Map<String, (Int) -> Boolean>): Boolean =
        mfcsamRules.all { (key, predicate) -> this[key]?.let { value -> predicate(value) } ?: true }

    override fun partOne(input: String) =
        input
            .lineSequence()
            .map(this::parseInput)
            .first { aunt ->
                aunt.properties.consistentWith(
                    mapOf(
                        "children" to { it == 3 },
                        "cats" to { it == 7 },
                        "samoyeds" to { it == 2 },
                        "pomeranians" to { it == 3 },
                        "akitas" to { it == 0 },
                        "vizslas" to { it == 0 },
                        "goldfish" to { it == 5 },
                        "trees" to { it == 3 },
                        "cars" to { it == 2 },
                        "perfumes" to { it == 1 },
                    )
                )
            }
            .id

    override fun partTwo(input: String) =
        input
            .lineSequence()
            .map(this::parseInput)
            .first { aunt ->
                aunt.properties.consistentWith(
                    mapOf(
                        "children" to { it == 3 },
                        "cats" to { it > 7 },
                        "samoyeds" to { it == 2 },
                        "pomeranians" to { it < 3 },
                        "akitas" to { it == 0 },
                        "vizslas" to { it == 0 },
                        "goldfish" to { it < 5 },
                        "trees" to { it > 3 },
                        "cars" to { it == 2 },
                        "perfumes" to { it == 1 },
                    )
                )
            }
            .id
}
