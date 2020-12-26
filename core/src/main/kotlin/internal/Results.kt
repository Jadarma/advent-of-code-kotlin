package aoc.core.internal

import aoc.core.AdventDay
import kotlin.time.Duration

/** The possible outcomes of executing a part of an [AdventDay]. */
internal sealed class PartResult {

    /** Nothing to do since it was either not implemented, or not applicable (such as some days missing a part two). */
    object NotImplemented : PartResult()

    /** Failed because it threw an uncaught exception. */
    data class Error(val cause: Throwable) : PartResult()

    /**
     * Did not finish exceptionally, but not all runs have the same return value.
     * This result can only be achieved if run more than once.
     * The [results] is a list of all values returned.
     */
    data class Inconsistent(val results: List<Any>) : PartResult()

    /** Completed successfully with a consistent [result], calculated in this [averageTime]. */
    data class Ok(val result: Any, val averageTime: Duration) : PartResult()
}

/** The metadata of an [AdventDay] and the execution results for both its parts. */
internal data class RunResult(
    val metadata: AdventMetadata,
    val partOneResult: PartResult,
    val partTwoResult: PartResult,
)
