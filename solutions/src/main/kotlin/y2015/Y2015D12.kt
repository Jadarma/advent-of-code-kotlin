package aoc.solutions.y2015

import aoc.core.AdventDay
import kotlinx.serialization.json.*

class Y2015D12 : AdventDay(2015, 12, "JSAbacusFramework.io") {

    /** Recursively find all integers in this Json structure and add them up. */
    private fun JsonElement.sum(): Int = when (this) {
        is JsonPrimitive -> content.toIntOrNull() ?: 0
        is JsonArray -> sumBy { it.sum() }
        is JsonObject -> values.sumBy { it.sum() }
    }

    /** Like [JsonElement.sum], but completely ignoring out all objects containing the value "red". */
    private fun JsonElement.sumIgnoringRed(): Int = when (this) {
        is JsonPrimitive -> content.toIntOrNull() ?: 0
        is JsonArray -> sumBy { it.sumIgnoringRed() }
        is JsonObject -> values
            .onEach { if (it is JsonPrimitive && it.content == "red") return 0 }
            .sumBy { it.sumIgnoringRed() }
    }

    override fun partOne(input: String) = Json.parseToJsonElement(input).sum()
    override fun partTwo(input: String) = Json.parseToJsonElement(input).sumIgnoringRed()
}
