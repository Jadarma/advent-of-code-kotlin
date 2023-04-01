package io.github.jadarma.aockt.test.internal

/**
 * Identifies an Advent of Code problem.
 *
 * @property year The year this problem appeared in.
 * @property day The day associated with this problem.
 */
public data class AdventDayID(
    public val year: Int,
    public val day: Int,
) : Comparable<AdventDayID> {

    init {
        require(year in VALID_YEAR_RANGE) { "Invalid year: '$year'." }
        require(day in VALID_DAY_RANGE) { "Invalid day: '$day'. " }
    }

    override fun toString(): String = "Y${year}D${day.toString().padStart(2, '0')}"

    @Suppress("MagicNumber")
    override fun compareTo(other: AdventDayID): Int = compareValuesBy(this, other) { year * 100 + day }

    private companion object {
        val VALID_DAY_RANGE = 1 .. 25
        val VALID_YEAR_RANGE = 2015 .. 9999
    }
}
