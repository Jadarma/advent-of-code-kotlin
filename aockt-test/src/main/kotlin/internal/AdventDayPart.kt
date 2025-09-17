package io.github.jadarma.aockt.test.internal

import io.github.jadarma.aockt.core.Solution
import io.github.jadarma.aockt.test.internal.AdventDayPart.One
import io.github.jadarma.aockt.test.internal.AdventDayPart.Two

/** Selector for a specific part of a [Solution]. */
internal enum class AdventDayPart { One, Two }

/** Internal wrapper for processing a puzzle solution. */
internal typealias PartFunction = (PuzzleInput) -> PuzzleAnswer

/** Convenience function to return the [PartFunction] of a specific [part] of the [Solution]. */
internal fun Solution.partFunction(part: AdventDayPart): PartFunction {
    val function: (String) -> Any = when (part) {
        One -> ::partOne
        Two -> ::partTwo
    }
    return { input: PuzzleInput -> PuzzleAnswer(function(input.toString()).toString()) }
}

/** Convenience function to return the expected output for a given [part] of the [PuzzleTestData]. */
internal fun PuzzleTestData.solutionToPart(part: AdventDayPart): PuzzleAnswer? = when (part) {
    One -> solutionPartOne
    Two -> solutionPartTwo
}
