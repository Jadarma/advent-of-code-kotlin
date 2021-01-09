package aoc.solutions.y2016

import aoc.core.AdventDay

class Y2016D02 : AdventDay(2016, 2, "Bathroom Security") {

    /** Parses the input and returns the list of instructions for obtaining the bathroom passcode. */
    private fun parseInput(input: String): List<List<Direction>> =
        input.lines().map { line -> line.map { Direction.valueOf(it.toString()) } }

    /** The directions and their effect on grid movement. */
    private enum class Direction(val xOffset: Int, val yOffset: Int) {
        U(-1, 0), D(1, 0), L(0, -1), R(0, 1)
    }

    /** Move one unit in the given [direction], but only if that point is one of the given [validPoints]. */
    private fun Pair<Int, Int>.move(direction: Direction, validPoints: Set<Pair<Int, Int>>): Pair<Int, Int> {
        val point = (first + direction.xOffset) to (second + direction.yOffset)
        return point.takeIf { it in validPoints } ?: this
    }

    /**
     * Finds the passcode for a given keypad by following instructions.
     *
     * @param instructions The list of instructions for each button press, split by lines.
     * @param startFrom The keypad button to start from when reading the first set of instructions.
     * @param keypad A map from a button's coordinates on the grid to the character it represents. Coordinates have the
     * origin in the top right corner.
     */
    private fun passcodeFor(
        instructions: List<List<Direction>>,
        startFrom: Pair<Int, Int>,
        keypad: Map<Pair<Int, Int>, Char>,
    ): String {
        require(startFrom in keypad.keys) { "Cannot start from this button because it's not on the keypad." }
        return instructions
            .runningFold(startFrom) { start, dirs -> dirs.fold(start) { pos, dir -> pos.move(dir, keypad.keys) } }
            .drop(1)
            .map(keypad::getValue)
            .joinToString("")
    }

    override fun partOne(input: String) =
        passcodeFor(
            instructions = parseInput(input),
            startFrom = 1 to 1,
            keypad = mapOf(
                0 to 0 to '1', 0 to 1 to '2', 0 to 2 to '3',
                1 to 0 to '4', 1 to 1 to '5', 1 to 2 to '6',
                2 to 0 to '7', 2 to 1 to '8', 2 to 2 to '9',
            ),
        )

    override fun partTwo(input: String): Any =
        passcodeFor(
            instructions = parseInput(input),
            startFrom = 2 to 0,
            keypad = mapOf(
                0 to 2 to '1',
                1 to 1 to '2', 1 to 2 to '3', 1 to 3 to '4',
                2 to 0 to '5', 2 to 1 to '6', 2 to 2 to '7', 2 to 3 to '8', 2 to 4 to '9',
                3 to 1 to 'A', 3 to 2 to 'B', 3 to 3 to 'C',
                4 to 2 to 'D',
            ),
        )
}
