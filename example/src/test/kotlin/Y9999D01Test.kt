import io.github.jadarma.aockt.test.AdventDay
import io.github.jadarma.aockt.test.AdventSpec

/**
 * A test for a fictitious puzzle.
 *
 * ```text
 * The input is a string of numbers separated by a comma.
 * Part 1: Return the sum of the odd numbers.
 * Part 2: Return the product of the numbers.
 * ```
 */
@AdventDay(9999, 1, "Magic Numbers")
class Y9999D01Test : AdventSpec(Y9999D01(), {

    partOne {
        "1,2,3" shouldOutput 4
        listOf("0", "2,4,6,8", "2,2,2,2") shouldAllOutput 0
        "1,2,5" shouldOutput 6
    }
    partTwo {
        "1,2,3" shouldOutput 6
    }
})
