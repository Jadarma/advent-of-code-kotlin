package io.github.jadarma.aockt.test.internal

import io.github.jadarma.aockt.core.Solution
import io.github.jadarma.aockt.test.internal.AdventDayPart.One
import io.github.jadarma.aockt.test.internal.AdventDayPart.Two

/** Selector for a specific part of a [Solution]. */
internal enum class AdventDayPart { One, Two }

/** Convenience function to return the expected output for a given [part] of the [PuzzleTestData]. */
internal fun PuzzleTestData.solutionToPart(part: AdventDayPart): PuzzleAnswer? = when(part) {
    One -> solutionPartOne
    Two -> solutionPartTwo
}

/** Convenience function to return the implementation function of a specific [part] of the [Solution]. */
internal fun Solution.partFunction(part: AdventDayPart): (String) -> Any = when(part) {
    One -> ::partOne
    Two -> ::partTwo
}
