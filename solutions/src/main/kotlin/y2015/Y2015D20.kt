package aoc.solutions.y2015

import aoc.core.AdventDay

class Y2015D20 : AdventDay(2015, 20, "Infinite Elves and Infinite Houses") {

    /**
     * Finds the first house to get enough presents from the infinite elves.
     *
     * @param target The minimum amount of gifts per house.
     * @param giftsPerVisit How many gifts each elf visit adds to the house total.
     * @param maxVisitsPerElf Elves will stop delivering after this many stops. A negative value means infinite visits.
     * @return The number of the first house to have received at least [target] gifts, or -1 if not possible.
     */
    private fun houseGifts(target: Int, giftsPerVisit: Int, maxVisitsPerElf: Int = -1): Int {
        if (target <= 0 || giftsPerVisit <= 0) return -1

        val maximumHouses = target / giftsPerVisit
        fun maxHouseFor(elf: Int) = when {
            maxVisitsPerElf < 0 -> maximumHouses
            else -> minOf(maximumHouses, elf * maxVisitsPerElf + 1)
        }

        return IntArray(maximumHouses) { 0 }
            .apply {
                for (elf in 1 until maximumHouses) {
                    for (number in elf until maxHouseFor(elf) step elf) {
                        this[number] += elf * giftsPerVisit
                    }
                }
            }
            .indexOfFirst { it >= target }
    }

    override fun partOne(input: String) = houseGifts(target = input.toInt(), giftsPerVisit = 10)
    override fun partTwo(input: String) = houseGifts(target = input.toInt(), giftsPerVisit = 11, maxVisitsPerElf = 50)
}
