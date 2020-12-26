package aoc.core.test

import aoc.core.AdventDay
import aoc.core.internal.InputReader
import org.junit.jupiter.api.Assumptions.assumeTrue
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * A base test class for testing [AdventDay] solutions.
 *
 * Once the correct solutions for the real input are known, they can be specified here.
 * This makes refactoring easier since it automatically checks the input / answer pair is correct.
 * The input data for this is taken from the same resource files as the runtime version.
 *
 * @property implementation The implementation you want to test.
 * @property partOneAnswer The actual answer to part one of this challenge, if known.
 * @property partTwoAnswer The actual answer to part two of this challenge, if known.
 */
@Suppress("MemberVisibilityCanBePrivate")
public abstract class AdventTest(
    private val implementation: AdventDay,
    private val partOneAnswer: Any? = null,
    private val partTwoAnswer: Any? = null,
) {
    /** The input read from the main resources. Lazy to prevent looking for it if the answers are undefined. */
    private val actualInput by lazy { InputReader.forDay(implementation) }

    /** Convenience function to call part one directly. */
    protected fun partOne(input: String): Any = implementation.partOne(input)

    /** Convenience function to call part two directly. */
    protected fun partTwo(input: String): Any = implementation.partTwo(input)

    @Test
    public fun `Answers partOne correctly`() {
        assumeTrue(partOneAnswer != null)
        assertEquals(partOneAnswer, implementation.partOne(actualInput), "Incorrect answer for part one.")
    }

    @Test
    protected fun `Answers partTwo correctly`() {
        assumeTrue(partTwoAnswer != null)
        assertEquals(partTwoAnswer, implementation.partTwo(actualInput), "Incorrect answer for part two.")
    }
}
