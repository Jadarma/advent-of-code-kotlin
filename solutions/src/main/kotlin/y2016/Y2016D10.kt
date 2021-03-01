package aoc.solutions.y2016

import aoc.core.AdventDay
import aoc.solutions.y2016.Y2016D10.Target.Bot
import aoc.solutions.y2016.Y2016D10.Target.Output
import aoc.solutions.y2016.Y2016D10.TargetType.BOT
import aoc.solutions.y2016.Y2016D10.TargetType.OUTPUT

class Y2016D10 : AdventDay(2016, 10, "Balance Bots") {

    /** The possible types of [TargetId]s to receive numbers. */
    private enum class TargetType { BOT, OUTPUT }

    /** Identifies a target by it's type and identifier. */
    private data class TargetId(val type: TargetType, val ordinal: Int)

    /** A possible instruction defining part of the simulation. */
    private sealed class Instruction(val botId: Int) {

        /** An instruction that tells the bot with the given [botId] to receive a new [value]. */
        class Take(botId: Int, val value: Int) : Instruction(botId)

        /** An instruction that tells the bot with the given [botId] who the targets for the low and high values are. */
        class Give(botId: Int, val lowTargetId: TargetId, val highTargetId: TargetId) : Instruction(botId)

        companion object {
            private val takeRegex = Regex("""value (\d+) goes to bot (\d+)""")
            private val giveRegex =
                Regex("""bot (\d+) gives low to (bot|output) (\d+) and high to (bot|output) (\d+)""")

            /** Returns the [Instruction] described by the [input], throwing [IllegalArgumentException] if invalid. */
            fun parse(input: String): Instruction {
                takeRegex.matchEntire(input)?.destructured?.let { (value, bot) ->
                    return Take(bot.toInt(), value.toInt())
                }
                giveRegex.matchEntire(input)?.destructured?.let { (bot, lowType, lowNumber, highType, highNumber) ->
                    return Give(
                        botId = bot.toInt(),
                        lowTargetId = TargetId(TargetType.valueOf(lowType.toUpperCase()), lowNumber.toInt()),
                        highTargetId = TargetId(TargetType.valueOf(highType.toUpperCase()), highNumber.toInt()),
                    )
                }
                throw IllegalArgumentException("Invalid input.")
            }
        }
    }

    /** An entity in the simulation that is able to be targeted and given numbers. */
    private sealed class Target(val id: TargetId) {
        abstract fun receive(value: Int)

        /** A target that can hold two values and give them to two other targets. */
        class Bot(id: Int) : Target(TargetId(BOT, id)) {
            lateinit var lowTargetId: TargetId
            lateinit var highTargetId: TargetId

            private val values = mutableListOf<Int>()

            /** Removes the numbers from this bots' hands and returns them. */
            fun takeNumbers(): Pair<Int, Int> {
                check(canGive()) { "Cannot take numbers from the bot since both hands are not full." }
                return (values[0] to values[1]).also { values.clear() }
            }

            /** A bot can give its numbers away if it holds one in both hands. */
            fun canGive(): Boolean = values.size == 2

            /** Puts the [value] in one of the bots' hands, if there is a free one available. */
            override fun receive(value: Int) {
                check(values.size < 2) { "Cannot receive more values. Bot has full hands." }
                values.add(value)
                values.sort()
            }
        }

        /** A target that may hold one value, and cannot interact otherwise. */
        class Output(id: Int) : Target(TargetId(OUTPUT, id)) {

            /** The value currently stored in this output. */
            var value: Int = 0
                private set

            /** Puts the [value] inside this output, replacing any previous value. */
            override fun receive(value: Int) {
                this.value = value
            }
        }

        companion object {
            /** Creates a default Target for the given [targetId]. */
            fun createFor(targetId: TargetId): Target = when (targetId.type) {
                BOT -> Bot(targetId.ordinal)
                OUTPUT -> Output(targetId.ordinal)
            }
        }
    }

    /**
     * Runs a simulation of inputs, bots, and outputs, and returns the answers to the two problems.
     *
     * @param instructions A list of instructions that define how the bots behave.
     * @param targetNumbers Which pair of numbers to keep an eye on during comparisons.
     *   The pair should be sorted.
     *
     * @return A pair of two numbers:
     *   - The _first_ is the id of the bot that compared the [targetNumbers] together, if any.
     *   - The _second_ is the product of the numbers stored in outputs 0, 1, and 2.
     */
    private fun runSimulation(instructions: List<Instruction>, targetNumbers: Pair<Int, Int>): Pair<Int?, Long> {
        require(targetNumbers.first <= targetNumbers.second) { "Target numbers must be sorted." }

        val states = mutableMapOf<TargetId, Target>()

        instructions.forEach {
            val affectedBot = states.getOrPut(TargetId(BOT, it.botId)) { Bot(it.botId) } as Bot
            when (it) {
                is Instruction.Take -> affectedBot.receive(it.value)
                is Instruction.Give -> affectedBot.apply {
                    states.getOrPut(it.lowTargetId) { Target.createFor(it.lowTargetId) }
                    states.getOrPut(it.highTargetId) { Target.createFor(it.highTargetId) }
                    lowTargetId = it.lowTargetId
                    highTargetId = it.highTargetId
                }
            }
        }

        var botThatCompares: Int? = null
        var transferComplete = false
        while (!transferComplete) {
            transferComplete = true
            states
                .values
                .filterIsInstance<Bot>()
                .filter { it.canGive() }
                .forEach { bot ->
                    transferComplete = false
                    val numbers = bot.takeNumbers()
                    if (numbers == targetNumbers) {
                        botThatCompares = bot.id.ordinal
                    }
                    states.getValue(bot.lowTargetId).receive(numbers.first)
                    states.getValue(bot.highTargetId).receive(numbers.second)
                }
        }

        val output = listOf(0, 1, 2)
            .map { TargetId(OUTPUT, it) }
            .map { states.getValue(it) as Output }
            .map { it.value.toLong() }
            .reduce(Long::times)

        return botThatCompares to output
    }

    override fun partOne(input: String) = runSimulation(
        instructions = input.lines().map(Instruction.Companion::parse),
        targetNumbers = 17 to 61,
    ).first!!

    override fun partTwo(input: String) = runSimulation(
        instructions = input.lines().map(Instruction.Companion::parse),
        targetNumbers = 17 to 61,
    ).second
}
