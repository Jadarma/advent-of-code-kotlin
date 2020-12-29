package aoc.solutions.y2015

import aoc.core.AdventDay

class Y2015D18 : AdventDay(2015, 18, "Like a GIF For Your Yard") {

    private class GameOfLife(val width: Int, val height: Int, val santaVariation: Boolean = false) {

        private val state = Array(width + 2) { BooleanArray(height + 2) { false } }

        init {
            stuckLights()
        }

        private fun validateCoordinates(x: Int, y: Int) {
            if (x !in 0 until width || y !in 0 until height)
                throw IndexOutOfBoundsException("Invalid coordinate: $x - $y")
        }

        operator fun get(x: Int, y: Int): Boolean {
            validateCoordinates(x, y)
            return state[x + 1][y + 1]
        }

        operator fun set(x: Int, y: Int, isAlive: Boolean) {
            validateCoordinates(x, y)
            state[x + 1][y + 1] = isAlive
        }

        /** Returns how many cells are alive in the current state of the game. */
        fun aliveCells() = state.sumBy { row -> row.count { it } }

        /** Passes a generation and updates the internal state. */
        fun playGeneration() {
            val currentGen = Array(state.size) { state[it].copyOf() }
            for (x in 1..width) {
                for (y in 1..height) {
                    val v = currentGen[x][y]
                    val n = currentGen.neighbourCount(x, y)
                    state[x][y] = when {
                        v && n in 2..3 -> true
                        !v && n == 3 -> true
                        else -> false
                    }
                }
            }
            stuckLights()
        }

        /** Returns how many neighbouring cells around this point are alive. */
        private fun Array<BooleanArray>.neighbourCount(x: Int, y: Int): Int {
            var sum = 0
            for (i in x - 1..x + 1) {
                for (j in y - 1..y + 1) {
                    sum += this[i][j].toInt()
                }
            }
            return sum - this[x][y].toInt()
        }

        private fun Boolean.toInt() = if (this) 1 else 0

        /** If this is the [santaVariation], override the four corner cells to be alive. */
        private fun stuckLights() {
            if (!santaVariation) return
            state[1][1] = true
            state[width][1] = true
            state[1][height] = true
            state[width][height] = true
        }
    }

    /** Parses the [input] lines and builds a new [GameOfLife] with the given initial state. */
    private fun parseInput(input: List<String>, santaVariation: Boolean = false) =
        GameOfLife(input.first().length, input.size, santaVariation).apply {
            input.forEachIndexed { y, string ->
                string.forEachIndexed { x, c ->
                    this[x, y] = when (c) {
                        '#' -> true
                        '.' -> false
                        else -> throw IllegalArgumentException("Invalid input.")
                    }
                }
            }
        }

    override fun partOne(input: String) = parseInput(input.lines()).run {
        repeat(100) { playGeneration() }
        aliveCells()
    }

    override fun partTwo(input: String) = parseInput(input.lines(), santaVariation = true).run {
        repeat(100) { playGeneration() }
        aliveCells()
    }
}
