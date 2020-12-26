package aoc.core

import aoc.core.internal.VALID_DAYS
import aoc.core.internal.VALID_YEARS

/** Validates and stores all possible options to configure the [AdventOfCode] class. */
public data class Configuration(
    val daySelector: DaySelector = DaySelector.All,
    val repeat: Int = 1,
    val enableColor: EnableColor = EnableColor.AUTO,
) {
    init {
        require(repeat > 0) { "Amount of execution repetitions must be positive." }
    }
}

/** A predicate for filtering the [AdventDay]s that should be run. */
public sealed class DaySelector {

    /** All detected days should be selected. */
    public object All : DaySelector()

    /** The last day in ascending chronological order (i.e. the latest date). */
    public object Last : DaySelector()

    /** Only days belonging to the specific [year] should be selected. */
    public data class Year(val year: Int) : DaySelector() {
        init {
            require(year in VALID_YEARS) { "Invalid year selector." }
        }
    }

    /** Only this specific [day] from this specific [year] should be selected. */
    public data class Single(val year: Int, val day: Int) : DaySelector() {
        init {
            require(year in VALID_YEARS) { "Invalid year selector." }
            require(day in VALID_DAYS) { "Invalid day selector." }
        }
    }
}

/** When to enable colored output. */
public enum class EnableColor { AUTO, ALWAYS, NEVER }
