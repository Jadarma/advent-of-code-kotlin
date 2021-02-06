package aoc.solutions.y2016

import aoc.core.AdventDay

class Y2016D04 : AdventDay(2016, 4, "Security Through Obscurity") {

    /** The actual value of a room, containing its real name and location. */
    private data class Room(val name: String, val sector: Int) {

        init {
            require(sector > 0) { "Invalid sector; must be positive." }
            require(name.all { it in "abcdefghijklmnopqrstuvwxyz " }) { "Invalid name; illegal character." }
            require(checksum.length == 5) { "Invalid name; not enough distinct characters." }
        }

        /** The encrypted [name], obtained by shifting by [sector] positions. */
        private val encryptedName: String get() = name.shift(-sector)

        /** The checksum of the [encryptedName], the top 5 letters sorted by occurrences, then alphabetically*/
        private val checksum: String
            get() = encryptedName
                .groupBy { it }
                .mapValues { it.value.count() }
                .filter { it.key != '-' }
                .entries.sortedWith(compareByDescending<Map.Entry<Char, Int>> { it.value }.thenBy { it.key })
                .take(5)
                .joinToString(separator = "") { it.key.toString() }

        override fun toString() = "$encryptedName-$sector[$checksum]"

        companion object {

            private val inputRegex = Regex("""([a-z-]+)-(\d+)\[([a-z]{5})]""")

            /**
             * Creates a string obtained by shifting every character by [shiftBy] positions, cyclically.
             * Accepts only lowercase letters, spaces, and dashes.
             * For the latter two, they are swapped around instead.
             */
            private fun String.shift(shiftBy: Int): String =
                map { char ->
                    when (char) {
                        ' ' -> '-'
                        '-' -> ' '
                        in 'a'..'z' -> 'a' + Math.floorMod((char - 'a') + shiftBy, 26)
                        else -> throw IllegalArgumentException("Can only rotate lowercase letters, dashes, and spaces.")
                    }
                }.joinToString("")

            /** Returns the original room of the encrypted [input] or null if it is invalid. */
            fun parseOrNull(input: String): Room? = runCatching {
                val (encryptedName, sectorRaw, _) = inputRegex.matchEntire(input)!!.destructured
                val sector = sectorRaw.toInt()
                val name = encryptedName.shift(sector)
                return Room(name, sector).takeIf { it.toString() == input }
            }.getOrNull()
        }
    }

    /** Parses the [input], returning only the valid [Room]s it contains. */
    private fun parseInput(input: String) = input.lineSequence().mapNotNull { Room.parseOrNull(it) }

    override fun partOne(input: String) = parseInput(input).sumBy { it.sector }
    override fun partTwo(input: String) = parseInput(input).first { it.name == "northpole object storage" }.sector
}
