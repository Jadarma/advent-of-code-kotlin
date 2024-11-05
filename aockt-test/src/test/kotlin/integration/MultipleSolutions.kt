package io.github.jadarma.aockt.test.integration

import io.github.jadarma.aockt.core.Solution
import io.github.jadarma.aockt.test.AdventDay
import io.github.jadarma.aockt.test.AdventSpec

/** A solution to a fictitious puzzle used for testing using collections. */
class Y9999D01UsingCollections : Solution {

    private fun parseInput(input: String): List<Int> =
        input
            .splitToSequence(',')
            .map(String::toInt)
            .toList()

    override fun partOne(input: String) = parseInput(input).filter { it % 2 == 1 }.sum()

    override fun partTwo(input: String) = parseInput(input).reduce { a, b -> a * b }
}

/** A solution to a fictitious puzzle used for testing using sequences. */
class Y9999D01UsingSequences : Solution {

    private fun parseInput(input: String): Sequence<Int> =
        input
            .splitToSequence(',')
            .map(String::toInt)

    override fun partOne(input: String) = parseInput(input).filter { it % 2 == 1 }.sum()

    override fun partTwo(input: String) = parseInput(input).reduce { a, b -> a * b }
}

@AdventDay(9999, 1, "Magic Numbers","Using Collections")
class Y9999D01CollectionsTest : Y9999D01Spec<Y9999D01UsingCollections>()

@AdventDay(9999, 1, "Magic Numbers", "Using Sequences")
class Y9999D01SequencesTest : Y9999D01Spec<Y9999D01UsingSequences>()

/**
 * A test for a fictitious puzzle.
 *
 * ```text
 * The input is a string of numbers separated by a comma.
 * Part 1: Return the sum of the odd numbers.
 * Part 2: Return the product of the numbers.
 * ```
 */
@Suppress("UnnecessaryAbstractClass")
abstract class Y9999D01Spec<T : Solution> : AdventSpec<T>({

    partOne {
        "1,2,3" shouldOutput 4
        listOf("0", "2,4,6,8", "2,2,2,2") shouldAllOutput 0
        "1,2,5" shouldOutput 6
    }

    partTwo {
        "1,2,3" shouldOutput 6
    }
})
