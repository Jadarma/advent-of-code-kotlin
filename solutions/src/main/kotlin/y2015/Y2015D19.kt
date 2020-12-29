package aoc.solutions.y2015

import aoc.core.AdventDay

class Y2015D19 : AdventDay(2015, 19, "Medicine for Rudolph") {

    /** Returns all the possible transformations the machine can do. */
    private fun parseInput(input: List<String>): List<Pair<String, String>> =
        input.dropLast(2).map {
            val (first, second) = it.split(" => ")
            first to second
        }

    override fun partOne(input: String): Int {
        val lines = input.lines()
        val molecule = lines.last()

        return parseInput(lines)
            .flatMap { (original, transformed) ->
                Regex(original)
                    .findAll(molecule)
                    .map { molecule.replaceRange(it.range, transformed) }
                    .toList()
            }
            .toSet()
            .size
    }

    override fun partTwo(input: String): Int {
        val lines = input.lines()
        val molecule = lines.last()
        val transformations = parseInput(lines).toMutableList()

        var iterations = 0
        var target = molecule

        while (target != "e") {
            val last = target
            transformations.forEach { (original, transformed) ->
                if (target.contains(transformed)) {
                    target = target.replaceFirst(transformed, original)
                    iterations++
                }
            }
            if (last == target) {
                transformations.shuffle()
                target = molecule
                iterations = 0
            }
        }

        return iterations
    }
}
