package io.github.jadarma.aockt.core


/**
 * An API for a solution to an Advent Day puzzle.
 *
 * Example:
 * ```kotlin
 * object Y9999D01 : Solution {
 *     override fun partOne(input: String) = input.length
 *     override fun partTwo(input: String) = input.first()
 * }
 * ```
 */
public interface Solution {

    /** Given this [input], computes and returns the resulting answer for part one of the puzzle. */
    public fun partOne(input: String): Any = TODO("Part 1 not implemented.")

    /**
     * Given this [input], computes and returns the resulting answer for part two of the puzzle.
     * For days that do not have a part two (25th), this function should not be implemented.
     */
    public fun partTwo(input: String): Any = TODO("Part 2 not implemented.")
}