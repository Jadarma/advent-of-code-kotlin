package io.github.jadarma.aockt.test.internal

import io.github.jadarma.aockt.core.Solution

/**
 * Reads [PuzzleTestData]s from classpath resources.
 *
 * Looks into the `/aockt` directory, which it expects to be split by year, then by day, then by type.
 *
 * For example, the following is the valid directory tree for `Y2015D01`:
 *
 * ```text
 * resources
 *  └─ aockt
 *    └─ y2015
 *      └─ d01
 *        ├─ input.txt
 *        ├─ solution_part1.txt
 *        └─ solution_part2.txt
 * ```
 */
internal object TestData {

    /** Returns the available [PuzzleTestData] for a given [AdventDayID] by reading it from the resources. */
    fun inputFor(adventDayID: AdventDayID): PuzzleTestData {
        val path = with(adventDayID) { "/aockt/y$year/d${day.toString().padStart(2, '0')}" }
        return PuzzleTestData(
            input = readResourceAsTextOrNull("$path/input.txt").toPuzzleInput(),
            solutionPartOne = readResourceAsTextOrNull("$path/solution_part1.txt").toPuzzleAnswer(),
            solutionPartTwo = when(adventDayID.day) {
                25 -> PuzzleAnswer.NO_SOLUTION
                else -> readResourceAsTextOrNull("$path/solution_part2.txt").toPuzzleAnswer()
            },
        )
    }

    /** Reads a resource at the given [path] and returns its contents as text, if it exists. */
    private fun readResourceAsTextOrNull(path: String): String? =
        this::class.java
            .getResourceAsStream(path)
            ?.use { String(it.readAllBytes()).trimEnd() }

    /** Wraps a [String] into a [PuzzleAnswer] type. */
    private fun String?.toPuzzleAnswer(): PuzzleAnswer? = when(this) {
        null -> null
        else -> PuzzleAnswer(this)
    }

    /** Wraps a [String] into a [PuzzleInput] type. */
    private fun String?.toPuzzleInput(): PuzzleInput? = when(this) {
        null -> null
        else -> PuzzleInput(this)
    }
}

/**
 * Data holder for the test data of a puzzle [Solution].
 *
 * @property input           The actual, user specific puzzle input, read from resources.
 * @property solutionPartOne The correct solution for part one given the input. If null, is currently unknown.
 * @property solutionPartTwo The correct solution for part two given the input. If null, is currently unknown.
 */
internal data class PuzzleTestData(
    val input: PuzzleInput?,
    val solutionPartOne: PuzzleAnswer?,
    val solutionPartTwo: PuzzleAnswer?,
)

/** The user-specific input to a puzzle. */
@JvmInline
internal value class PuzzleInput(private val input: String) {

    init {
        require(input.isNotBlank()) { "A puzzle input must be a non-blank string!" }
    }

    override fun toString(): String = input

    /** Formats the input in a printable friendly manner. */
    @Suppress("MagicNumber")
    fun preview(): String = when(input.count { it == '\n' }) {
        0 -> input
        in 1 .. 5 -> "\n$input\n"
        else -> buildString {
            val lines = input.lines()
            appendLine()
            lines.take(3).forEach(::appendLine)
            appendLine("...")
            appendLine(lines.last())
        }
    }
}

/** An answer given by a [Solution] that was given a [PuzzleInput]. */
@JvmInline
internal value class PuzzleAnswer(private val answer: String) {
    override fun toString() = answer

    companion object {
        /** In the case of some days, there is no part two requirement. */
        val NO_SOLUTION = PuzzleAnswer("=== No Solution ===")
    }
}
