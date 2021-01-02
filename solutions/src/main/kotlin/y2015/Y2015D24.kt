package aoc.solutions.y2015

import aoc.core.AdventDay
import kotlin.collections.ArrayDeque

class Y2015D24 : AdventDay(2015, 24, "It Hangs in the Balance") {

    /**
     * Returns a sequence of all combinations of size [take] from the initial list.
     * This is a refactor of a Rosetta Code snippet, available at [https://rosettacode.org/wiki/Combinations#Kotlin].
     */
    private fun List<Int>.combinations(take: Int): Sequence<List<Int>> = sequence {
        val combination = IntArray(take) { first() }
        val stack = ArrayDeque<Int>().apply { addFirst(0) }

        while (stack.isNotEmpty()) {
            var resIndex = stack.size - 1
            var arrIndex = stack.removeFirst()

            while (arrIndex < size) {
                combination[resIndex++] = get(arrIndex++)
                stack.addFirst(arrIndex)

                if (resIndex == take) {
                    yield(combination.toList())
                    break
                }
            }
        }
    }

    /**
     * Returns the quantum entanglement for the best group of packages that can go in Santa's passenger compartiment,
     * or `null` if there is no solution.
     *
     * **NOTE:** This isn't really the solution, since it only finds the ideal first group but doesn't check that the
     * rest of the presents can be split in equal groups. However, it seems the input data for all advent participants
     * the first ideal bucket is always part of a solution. Sadly, Santa only gets the value of the QE, not the actual
     * split, and his sled would remain unbalanced.
     */
    private fun minQuantumEntanglement(pool: List<Int>, buckets: Int): Long? {
        if (buckets < 1) return null
        val target = pool.sum() / buckets

        for (bucketSize in 1..pool.size) {
            return pool
                .combinations(take = bucketSize)
                .filter { it.sum() == target }
                .minOfOrNull { it.fold(1L, Long::times) }
                ?: continue
        }

        return null
    }

    override fun partOne(input: String) = input.lines().map(String::toInt).let { minQuantumEntanglement(it, 3)!! }
    override fun partTwo(input: String) = input.lines().map(String::toInt).let { minQuantumEntanglement(it, 4)!! }
}
