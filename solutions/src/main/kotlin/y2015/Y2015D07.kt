package aoc.solutions.y2015

import aoc.core.AdventDay

@ExperimentalUnsignedTypes
class Y2015D07 : AdventDay(2015, 7, "Some Assembly Required") {

    // Regular expressions for parsing some language tokens.
    private val variableRegex = Regex("""\b[a-z]+\b""")
    private val constantRegex = Regex("""\b\d+\b""")
    private val unary = Regex("""^([A-Z]+) (\d+)$""")
    private val binary = Regex("""^(\d+) ([A-Z]+) (\d+)$""")

    /**
     * Evaluates a valid expression built with the limited instruction set, or throws if invalid syntax.
     * Does not accept variables, only constant values.
     */
    private fun evaluate(expression: String): UShort {
        constantRegex.matchEntire(expression)?.run {
            return value.toUShort()
        }

        unary.matchEntire(expression)?.run {
            val (operator, valueString) = destructured
            val value = valueString.toUShort()
            return when (operator) {
                "NOT" -> value.inv()
                else -> throw IllegalArgumentException("Invalid unary operator: $operator")
            }
        }

        binary.matchEntire(expression)?.run {
            val (leftString, operator, rightString) = destructured
            val left = leftString.toUShort()
            val right = rightString.toUShort()
            return when (operator) {
                "AND" -> left and right
                "OR" -> left or right
                "LSHIFT" -> left.toInt().shl(right.toInt()).toUShort()
                "RSHIFT" -> left.toInt().shr(right.toInt()).toUShort()
                else -> throw IllegalArgumentException("Invalid binary operator: $operator")
            }
        }

        throw IllegalArgumentException("Invalid expression: $expression")
    }

    /**
     * Takes some [instructions], and builds and evaluates the state of its wires.
     * Returns a map of each wire label to its signal value.
     */
    private fun evalCircuit(instructions: List<String>): Map<String, UShort> {
        val state = mutableMapOf<String, UShort>()
        val evaluationQueue =
            instructions
                .map { it.split(" -> ") }
                .map { it[0] to it[1] }
                .let(::ArrayDeque)

        while(evaluationQueue.isNotEmpty()) {
            val instruction = evaluationQueue.removeFirst()

            var allInputsAvailable = true
            val lhs = variableRegex.replace(instruction.first) {
                when (val stateValue = state[it.value]) {
                    null -> "null".also { allInputsAvailable = false }
                    else -> stateValue.toString()
                }
            }

            if(!allInputsAvailable) {
                evaluationQueue.addLast(instruction)
                continue
            }

            state[instruction.second] = evaluate(lhs)
        }

        return state
    }

    override fun partOne(input: String) = evalCircuit(input.lines()).getValue("a")

    override fun partTwo(input: String): UShort {
        val a = partOne(input)
        val overriddenInput = input.lineSequence().map { if(it.endsWith(" -> b")) "$a -> b" else it }.toList()
        return evalCircuit(overriddenInput).getValue("a")
    }
}
