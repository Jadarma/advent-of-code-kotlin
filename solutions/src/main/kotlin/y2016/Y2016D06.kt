package aoc.solutions.y2016

import aoc.core.AdventDay

class Y2016D06 : AdventDay(2016, 6, "Signals and Noise") {

    private fun Sequence<String>.filterNoise(selector: Iterable<Map.Entry<Char, Int>>.() -> Map.Entry<Char, Int>) =
        List(first().length) { mutableMapOf<Char, Int>() }
            .apply {
                this@filterNoise.forEach { line ->
                    line.forEachIndexed { index, char ->
                        val current = get(index).getOrDefault(char, 0)
                        get(index)[char] = current + 1
                    }
                }
            }
            .map { it.entries.selector().key }
            .joinToString("")

    override fun partOne(input: String) = input.lineSequence().filterNoise { maxByOrNull { it.value }!! }
    override fun partTwo(input: String) = input.lineSequence().filterNoise { minByOrNull { it.value }!! }
}
