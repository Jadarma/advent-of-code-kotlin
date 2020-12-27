package aoc.solutions.y2015

import aoc.core.AdventDay
import aoc.solutions.y2015.Y2015D06.Action.*
import kotlin.math.abs

class Y2015D06 : AdventDay(2015, 6, "Probably a Fire Hazard") {

    private enum class Action { TURN_ON, TOGGLE, TURN_OFF }

    private data class Rectangle(val x1: Int, val y1: Int, val x2: Int, val y2: Int)

    /** Regex that can parse the syntax of Santa's instructions. */
    private val instructionRegex = Regex("""(toggle|turn off|turn on) (\d+),(\d+) through (\d+),(\d+)""")

    private fun String.parseInstruction(): Pair<Action, Rectangle> = runCatching {
        val (action_raw, x1, y1, x2, y2) = instructionRegex.matchEntire(this)!!.destructured
        val action = Action.valueOf(action_raw.replace(' ', '_').toUpperCase())
        val rectangle = Rectangle(x1.toInt(), y1.toInt(), x2.toInt(), y2.toInt())
        action to rectangle
    }.getOrElse { throw IllegalArgumentException("Invalid instruction: $this") }

    private class LightGrid(size: Int) {

        private val grid = Array(size) { Array(size) { 0 } }

        /**
         * Returns the total sum of values for all cells in the grid.
         * This is somewhat prone to overflowing. It is safe up until a value of 2000 brightness per cell.
         */
        fun sum() = grid.sumBy { it.sum() }

        /** For all points that overlap the [rectangle], update the cell value by applying the [mapper] function. */
        fun mapRectangle(rectangle: Rectangle, mapper: (Int) -> Int) {
            with(rectangle) {
                for (x in x1..x2) {
                    for (y in y1..y2) {
                        grid[x][y] = mapper(grid[x][y])
                    }
                }
            }
        }
    }

    override fun partOne(input: String) = with(LightGrid(1000)) {
        input
            .lineSequence()
            .map { it.parseInstruction() }
            .forEach { (action, rectangle) ->
                mapRectangle(rectangle) { value ->
                    when (action) {
                        TURN_ON -> 1
                        TOGGLE -> abs(value - 1)
                        TURN_OFF -> 0
                    }
                }
            }
        sum()
    }

    override fun partTwo(input: String) = with(LightGrid(1000)) {
        input
            .lineSequence()
            .map { it.parseInstruction() }
            .forEach { (action, rectangle) ->
                mapRectangle(rectangle) { value ->
                    when (action) {
                        TURN_ON -> value + 1
                        TOGGLE -> value + 2
                        TURN_OFF -> maxOf(value - 1, 0)
                    }
                }
            }
        sum()
    }
}
