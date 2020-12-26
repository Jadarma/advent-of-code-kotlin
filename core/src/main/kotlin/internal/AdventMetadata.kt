package aoc.core.internal

/** Data holder for information about an advent day. */
internal data class AdventMetadata(val year: Int, val day: Int, val title: String) {

    init {
        require(year in VALID_YEARS) { "Invalid year." }
        require(day in VALID_DAYS) { "Invalid day." }
    }

    /** Prints the day as an `Y<YYYY>D<DD>` format, e.g: `Y2015D01`. */
    override fun toString() = "Y${year}D${day.toString().padStart(2, '0')}"

    /** The URL of the challenge, where details are available. */
    val url get() = "https://adventofcode.com/$year/day/$day"
}
