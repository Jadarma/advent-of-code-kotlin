package aoc.solutions.y2016

import aoc.core.AdventDay
import aoc.solutions.y2016.Y2016D01.Direction.*
import kotlin.math.absoluteValue

class Y2016D01 : AdventDay(2016, 1, "No Time for a Taxicab") {

    /** Parses the input and returns the sequence of turn and direction pairs. */
    private fun parseInput(input: String): Sequence<Pair<Boolean, Int>> =
        Regex("""(?:, )?([LR])(\d+)""")
            .findAll(input)
            .map {
                val (turn, distance) = it.destructured
                val clockwise = when (turn.first()) {
                    'R' -> true
                    'L' -> false
                    else -> throw IllegalArgumentException("Can only turn (L)eft or (R)ight.")
                }
                clockwise to distance.toInt()
            }

    /** The cardinal directions and their effect on grid movement. */
    private enum class Direction(val horizontalMultiplier: Int, val verticalMultiplier: Int) {
        North(0, 1), East(1, 0), South(0, -1), West(-1, 0)
    }

    /** Holds a position on the grid, and the direction you are facing. */
    private data class Position(val x: Int, val y: Int, val direction: Direction)

    /** Keep the current coordinates but change the direction by turning [clockwise] or not 90 degrees. */
    private fun Position.turn(clockwise: Boolean) = copy(direction = when (direction) {
        North -> if (clockwise) West else East
        East -> if (clockwise) North else South
        South -> if (clockwise) East else West
        West -> if (clockwise) South else North
    })

    /** Move [distance] units in the current [Position.direction]. */
    private fun Position.move(distance: Int) = copy(
        x = x + distance * direction.horizontalMultiplier,
        y = y + distance * direction.verticalMultiplier,
    )

    /** Returns the distance between this position and the origin point (0,0). */
    private val Position.distanceFromOrigin get() = x.absoluteValue + y.absoluteValue

    override fun partOne(input: String) =
        parseInput(input)
            .fold(Position(0, 0, North)) { pos, instr -> pos.turn(instr.first).move(instr.second) }
            .distanceFromOrigin

    override fun partTwo(input: String): Int {
        val visited = mutableSetOf(0 to 0)
        parseInput(input)
            .fold(Position(0, 0, North)) { pos, instr ->
                (1..instr.second).fold(pos.turn(instr.first)) { it, _ ->
                    it.move(1).apply {
                        with(x to y) {
                            if (this in visited) return distanceFromOrigin
                            visited.add(this)
                        }
                    }
                }
            }

        return -1
    }
}
