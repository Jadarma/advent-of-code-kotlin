package aoc.core.internal

import aoc.core.AdventDay
import aoc.core.DaySelector
import org.reflections.Reflections
import org.reflections.util.ConfigurationBuilder

/** Container for storing references to all known [AdventDay] implementation in the project using reflection. */
internal object AdventCalendar {

    private val reflections = Reflections(
        ConfigurationBuilder()
            .forPackages("aoc.solutions")
            .addClassLoader(ClassLoader.getSystemClassLoader())
    )

    private val all: List<AdventDay> =
        reflections
            .getSubTypesOf(AdventDay::class.java)
            .map { it.kotlin.constructors.first().call() }
            .sorted()
            .also {
                val duplicates = it - it.distinctBy(AdventDay::hashCode)
                require(duplicates.isEmpty()) { "Duplicate implementations found for ${duplicates.joinToString()}" }
            }

    fun select(daySelector: DaySelector): List<AdventDay> = when (daySelector) {
        is DaySelector.All -> all
        is DaySelector.Last -> all.takeLast(1)
        is DaySelector.Year -> all.filter { it.metadata.year == daySelector.year }
        is DaySelector.Single -> all.filter { it.metadata.year == daySelector.year && it.metadata.day == daySelector.day }
    }
}
