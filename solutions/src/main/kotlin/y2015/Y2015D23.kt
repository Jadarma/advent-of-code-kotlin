package aoc.solutions.y2015

import aoc.core.AdventDay

@ExperimentalUnsignedTypes
class Y2015D23 : AdventDay(2015, 23, "Opening the Turing Lock") {

    /** Parses the input and returns the list of instructions. */
    private fun parseInput(input: String): List<Instruction> =
        input.lineSequence().map(Instruction.Companion::parse).toList()

    /** A mutable state object that holds the registry memory of the computer. */
    @ExperimentalUnsignedTypes
    private class State(initialRegisters: Map<String, UInt> = emptyMap()) {
        private val registers: MutableMap<String, UInt> = initialRegisters.toMutableMap()

        // Convenience operators to add null-safety to indexing operations.
        operator fun get(register: String) = registers.getOrPut(register) { 0.toUInt() }
        operator fun set(register: String, value: UInt) = registers.put(register, value)

        /**
         * Convenience property for working with the instruction pointer as a signed integer, as is often the case
         * when applying offsets. The actual unsigned version is accessible from `state["ip"]` as well.
         */
        var ip: Int
            get() = this["ip"].toInt()
            set(value) {
                this["ip"] = value.toUInt()
            }

        override fun toString() = registers.toString()
    }

    /** An instruction executable by the computer. */
    private sealed class Instruction {

        data class Half(val register: String) : Instruction()
        data class Triple(val register: String) : Instruction()
        data class Increment(val register: String) : Instruction()
        data class Jump(val offset: Int) : Instruction()
        data class JumpIfEven(val register: String, val offset: Int) : Instruction()
        data class JumpIfOne(val register: String, val offset: Int) : Instruction()

        /** Applies the logic of this instruction on the [state], mutating it. */
        fun execute(state: State) {
            when (this) {
                is Half -> {
                    state[this.register] /= 2.toUInt()
                    state.ip++
                }
                is Triple -> {
                    state[this.register] *= 3.toUInt()
                    state.ip++
                }
                is Increment -> {
                    state[this.register]++
                    state.ip++
                }
                is Jump -> state.ip += offset
                is JumpIfEven -> state.ip += if (state[this.register] % 2.toUInt() == 0.toUInt()) offset else 1
                is JumpIfOne -> state.ip += if (state[this.register] == 1.toUInt()) offset else 1
            }
        }

        companion object {

            /** All valid instructions follow this syntax. */
            private val syntax = Regex("""^([a-z]\w+) ([+\-]\d+|\w+)(?:, ([+\-]\d+|\w+))*$""")

            /** Parses the [input] into an [Instruction], or throws an [IllegalArgumentException] if it is invalid. */
            fun parse(input: String): Instruction = runCatching {
                val tokens = syntax.matchEntire(input)!!.groupValues.drop(1)
                when (tokens[0]) {
                    "hlf" -> Half(tokens[1])
                    "tpl" -> Triple(tokens[1])
                    "inc" -> Increment(tokens[1])
                    "jmp" -> Jump(tokens[1].toInt())
                    "jie" -> JumpIfEven(tokens[1], tokens[2].toInt())
                    "jio" -> JumpIfOne(tokens[1], tokens[2].toInt())
                    else -> throw Exception("Invalid opcode.")
                }
            }.getOrElse { throw IllegalArgumentException("Invalid syntax.") }
        }
    }

    /**
     * Runs the program defined by the given [instructions] and returns the computer's [State] upon finishing
     * execution. Note the [State] reference is the same, just mutated.
     */
    private fun State.runProgram(instructions: List<Instruction>): State = apply {
        while (ip in instructions.indices) {
            instructions[ip].execute(this)
        }
    }

    override fun partOne(input: String) = State().runProgram(parseInput(input))["b"]
    override fun partTwo(input: String) = State(mapOf("a" to 1.toUInt())).runProgram(parseInput(input))["b"]
}
