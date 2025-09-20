package io.github.jadarma.aockt.test.internal

import io.github.jadarma.aockt.test.AdventDay

/**
 * Identifies an Advent of Code problem.
 *
 * @property year The year this problem appeared in.
 * @property day  The day associated with this problem.
 */
@Suppress("MagicNumber")
internal data class AdventDayID(val year: Int, val day: Int) : Comparable<AdventDayID> {

    init {
        require(year in 2015..9999) { "Invalid year: '$year'." }
        require(day in 1..25) { "Invalid day: '$day'. " }
    }

    override fun toString(): String = "Y${year}D${day.toString().padStart(2, '0')}"
    override fun compareTo(other: AdventDayID): Int = compareValuesBy(a = this, b = other) { it.year * 100 + it.day }
}

/** The internal typesafe [AdventDayID] for this [AdventDay]. */
internal val AdventDay.id: AdventDayID get() = AdventDayID(year, day)
