package aoc.core.internal

import aoc.core.AdventDay
import kotlin.time.measureTimedValue
import kotlin.time.milliseconds

/** Handles running and "benchmarking" [AdventDay] solution candidates. */
internal interface Runner {
    fun run(adventDay: AdventDay): RunResult
}

internal class RunnerImpl(private val repeat: Int) : Runner {

    /**
     * Execute the code for the given [adventDay], optionally [repeat] ing it several times and return a pair of
     * [PartResult] describing the outcome of the first and second parts of the challenge.
     */
    override fun run(adventDay: AdventDay): RunResult {
        val input = InputReader.forDay(adventDay)
        return RunResult(
            adventDay.metadata,
            runPart(input, repeat, adventDay::partOne),
            runPart(input, repeat, adventDay::partTwo),
        )
    }

    private fun runPart(input: String, repeat: Int, block: (String) -> Any): PartResult =
        runCatching {
            val timedResults = (0 until repeat).map { measureTimedValue { block(input) } }
            val results = timedResults.map { it.value }

            if (results.distinct().size != 1) {
                return PartResult.Inconsistent(results)
            }

            val averageTime = timedResults.map { it.duration.inMilliseconds }.average().milliseconds
            PartResult.Ok(results.first(), averageTime)
        }.getOrElse { exception ->
            return when (exception) {
                is NotImplementedError -> PartResult.NotImplemented
                else -> PartResult.Error(exception)
            }
        }
}
