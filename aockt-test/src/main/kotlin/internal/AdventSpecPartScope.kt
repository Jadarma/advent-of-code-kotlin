package io.github.jadarma.aockt.test.internal

import io.github.jadarma.aockt.test.AdventSpec
import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.withClue
import io.kotest.core.spec.style.scopes.ContainerScope
import io.kotest.core.spec.style.scopes.FunSpecContainerScope
import io.kotest.matchers.shouldBe

/**
 * A [ContainerScope] specialized for testing the outputs of a specific function against predetermined inputs.
 *
 * @param implementation The function to be tested.
 * @param context The parent context in which to append these test cases.
 */
internal class AdventSpecPartScope(
    private val implementation: (String) -> Any,
    private val context: FunSpecContainerScope,
) : AdventSpec.PartScope, ContainerScope by context {

    /** Incrementing counter for the example test cases. */
    private var exampleNumber: Int = 0

    override suspend infix fun String.shouldOutput(expected: Any) {
        exampleNumber++
        val input = PuzzleInput(this)
        val preview = input.preview()
        context.test("Example #$exampleNumber") {
            withClue("Expected answer '$expected' for input: $preview") {
                val answer = shouldNotThrowAny {
                    this@AdventSpecPartScope.implementation(input.toString()).toString()
                }
                answer shouldBe expected.toString()
            }
        }
    }

    override suspend infix fun Iterable<String>.shouldAllOutput(expected: Any) {
        forEach { it shouldOutput expected }
    }
}
