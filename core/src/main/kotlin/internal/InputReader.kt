package aoc.core.internal

import aoc.core.AdventDay

/** Helper object for reading problem inputs.*/
internal object InputReader {

    /** Reads the input for a given [adventDay] from the Resources directory. */
    fun forDay(adventDay: AdventDay): String {
        val resourcePath = "/aoc/input/y${adventDay.metadata.year}/input_${adventDay}.txt"
        val inputStreamReader = this::class.java.getResourceAsStream(resourcePath)?.reader()
            ?: throw IllegalArgumentException("Missing input for $adventDay")
        return inputStreamReader.readText().trimEnd()
    }
}
