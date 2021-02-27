package aoc.solutions.y2016

import aoc.core.AdventDay

class Y2016D09 : AdventDay(2016, 9, "Explosives in Cyberspace") {

    /** The expression that parses a compression marker, denoting how many characters to repeat how many times. */
    private val repeatRegex = Regex("""\((\d+)x(\d+)\)""")

    /**
     * Returns the unpacked length of the [input] if it would be decompressed. Use along with [allowRecursion] to check
     * for final length and check for fork bombs.
     */
    private fun unpackedLengthOf(input: String, allowRecursion: Boolean = false): Long {
        var index = 0
        var decodedLength = 0L
        while (index < input.length) {
            val match = repeatRegex.find(input, index)
            if (match == null || match.range.first != index) {
                decodedLength++
                index++
                continue
            }
            val markerSize = match.value.length
            val size = match.destructured.component1().toInt()
            val repeats = match.destructured.component2().toInt()

            decodedLength += repeats * when (allowRecursion) {
                true -> unpackedLengthOf(input.substring(index + markerSize, index + markerSize + size), true)
                else -> size.toLong()
            }
            index += size + markerSize
        }
        return decodedLength
    }

    override fun partOne(input: String) = unpackedLengthOf(input, allowRecursion = false)
    override fun partTwo(input: String) = unpackedLengthOf(input, allowRecursion = true)
}
