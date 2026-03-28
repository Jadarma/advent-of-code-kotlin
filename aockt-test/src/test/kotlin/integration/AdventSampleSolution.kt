package io.github.jadarma.aockt.integration

import io.github.jadarma.aockt.Solution

/**
 * The Sample problem:
 * - Part 1: Return the input, prepended by `1:`
 * - Part 2: Return the length of the input, prepended by `2:`
 *
 * The same problem is used for multiple days, indexed as follows:
 * - Day 1: Contains no files.
 * - Day 2: Has the input available.
 * - Day 3: Has the input and correct part one solution.
 * - Day 4: Has the input and both correct part solutions.
 */

/** Implements nothing. */
object SampleEmpty : Solution

/** Only implements part one. */
object SamplePartial : Solution {
    override fun partOne(input: String) = "1:$input"
}

/** Correct implementation. */
object SampleCorrect : Solution {
    override fun partOne(input: String) = "1:$input"
    override fun partTwo(input: String) = "2:${input.length}"
}

/** Passes only one example. */
object SampleOnlyExample : Solution {
    override fun partOne(input: String) = if (input == "pass") "1:$input" else "1:fail"
    override fun partTwo(input: String) = if (input == "pass") "2:${input.length}" else "2:fail"
}

/** Works for all examples, but not the input. */
object SampleOnlyEdgeCase : Solution {
    override fun partOne(input: String) = if (input == "ABC") "1:fail" else "1:$input"
    override fun partTwo(input: String) = if (input == "ABC") "2:fail" else "2:${input.length}"
}

/** Only works for the input, not the examples. */
object SampleOnlyInput : Solution {
    override fun partOne(input: String) = if (input == "ABC") "1:$input" else "1:fail"
    override fun partTwo(input: String) = if (input == "ABC") "2:${input.length}" else "2:fail"
}

/** Throws an exception for all inputs. */
object SampleError : Solution {
    override fun partOne(input: String) = throw NullPointerException("Boom!")
    override fun partTwo(input: String) = throw ArithmeticException("Boom!")
}
