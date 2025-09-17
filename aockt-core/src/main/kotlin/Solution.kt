package io.github.jadarma.aockt.core

/**
 * An API for a solution to an Advent Day puzzle.
 *
 * An implementation **must**:
 * - be an `object` or have a no-arg constructor.
 * - have independent functions (i.e.: it should be able to solve the second part of the puzzle without having invoked
 *   the first).
 *
 * Best practice recommendations:
 * - The part functions should be stateless and have no side effects.
 * - Keep as much of the solution within your type (except very generic helpers you don't wish to copy-paste).
 * - Keep all other members, inner classes, and helper functions as `private`, to simulate a black-box approach.
 * - Include the puzzle date in the name of the class, to make it easier to search for when sharing your solutions.
 *
 * Example:
 * ```kotlin
 * object Y9999D01 : Solution {
 *     private fun parseInput(input: String): Sequence<Int> = input.splitToSequence(',').map(String::toInt)
 *     override fun partOne(input: String) = input.sumOf { it >= 42 }
 *     override fun partTwo(input: String) = input.filter { it < 0 }.count()
 * }
 * ```
 */
public interface Solution {

    /**
     * Given this [input], computes and returns the resulting answer for part one of the puzzle.
     * The function should be pure.
     */
    public fun partOne(input: String): Any = throw NotImplementedError("Part 1 not implemented.")

    /**
     * Given this [input], computes and returns the resulting answer for part two of the puzzle.
     * For days that do not have a part two (25th), this function should not be implemented.
     * The function should be pure.
     */
    public fun partTwo(input: String): Any = throw NotImplementedError("Part 2 not implemented.")
}
