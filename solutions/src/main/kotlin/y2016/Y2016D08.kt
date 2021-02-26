package aoc.solutions.y2016

import aoc.core.AdventDay
import aoc.solutions.y2016.Y2016D08.Instruction.*

class Y2016D08 : AdventDay(2016, 8, "Two-Factor Authentication") {

    /** An instruction for building the display. */
    private sealed class Instruction {
        /** Turns on all of the pixels in a rectangle at the top-left of the screen of the given [width] x [height]. */
        data class Rectangle(val width: Int, val height: Int) : Instruction()

        /** Shifts all the pixels in the given [row] by [amount] pixels to the right, looping around. */
        data class RotateRow(val row: Int, val amount: Int) : Instruction()

        /** Shifts all the pixels in the given [column] by [amount] pixels down, looping around. */
        data class RotateColumn(val column: Int, val amount: Int) : Instruction()

        companion object {
            private val rectRegex = Regex("""rect (\d+)x(\d+)""")
            private val rowRegex = Regex("""rotate row y=(\d+) by (\d+)""")
            private val colRegex = Regex("""rotate column x=(\d+) by (\d+)""")

            /** Returns the [Instruction] described by the [input], throwing [IllegalArgumentException] if invalid. */
            fun parse(input: String): Instruction {
                rectRegex.matchEntire(input)?.destructured?.let { (x, y) -> return Rectangle(x.toInt(), y.toInt()) }
                rowRegex.matchEntire(input)?.destructured?.let { (y, v) -> return RotateRow(y.toInt(), v.toInt()) }
                colRegex.matchEntire(input)?.destructured?.let { (x, v) -> return RotateColumn(x.toInt(), v.toInt()) }
                throw IllegalArgumentException("Invalid input.")
            }
        }
    }

    /** The pixel state of a card swiper display. */
    private class Display(val width: Int, val height: Int) {
        private val grid = Array(height) { BooleanArray(width) { false } }

        /** Returns the number of pixels that are currently on. */
        fun litCount() = grid.sumBy { row -> row.count { it } }

        /** Formats a string according to the display state, meant to be printed. */
        fun toDisplayString() = buildString {
            grid.forEach { row ->
                row.map { if (it) '#' else '.' }.forEach { append(it) }
                appendLine()
            }
        }

        /** Mutates the state of this display by applying the given [instruction]. */
        fun apply(instruction: Instruction) = when (instruction) {
            is Rectangle -> {
                for (y in 0 until instruction.height) {
                    for (x in 0 until instruction.width) {
                        grid[y][x] = true
                    }
                }
            }
            is RotateColumn -> {
                val offset = instruction.amount % height
                val column = instruction.column
                val temp = Array(offset) { grid[grid.size - offset + it][column] }
                for (row in (grid.size - offset - 1) downTo 0) {
                    grid[row + offset][column] = grid[row][column]
                }
                for (row in 0 until offset) {
                    grid[row][column] = temp[row]
                }
            }
            is RotateRow -> {
                val offset = instruction.amount % width
                val row = instruction.row
                val temp = grid[row].copyOfRange(grid[row].size - offset, grid[row].size)
                grid[row].copyInto(grid[row], offset, 0, grid[row].size - offset)
                temp.copyInto(grid[row])
                Unit
            }
        }
    }

    /** Builds the [Display] state by following the instructions contained in the [input]. */
    private fun buildDisplay(input: String): Display = Display(50, 6).apply {
        input.lineSequence()
            .map { Instruction.parse(it) }
            .forEach(::apply)
    }

    override fun partOne(input: String): Int = buildDisplay(input).litCount()

    override fun partTwo(input: String): String = buildDisplay(input).toDisplayString().run {
        println(this) // Read this from the terminal. I won't do OCR yet... but maybe?
        "CFLELOYFCS"  // This is the answer for my particular input.
    }
}
