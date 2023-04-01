package io.github.jadarma.aockt.test

import io.github.jadarma.aockt.test.internal.PuzzleInput
import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.withClue
import io.kotest.core.spec.KotestTestScope
import io.kotest.core.spec.style.scopes.ContainerScope
import io.kotest.core.spec.style.scopes.FunSpecContainerScope
import io.kotest.matchers.shouldBe

/**
 * A [ContainerScope] specialized for testing the outputs of a specific function against predetermined inputs.
 *
 * @property implementation The function to be tested.
 * @property context The parent context in which to append these test cases.
 */
@KotestTestScope
public class AdventSpecExampleContainerScope(
    private val implementation: (String) -> Any,
    private val context: FunSpecContainerScope,
) : ContainerScope by context {

    /** Incrementing counter for the example test cases. */
    private var exampleNumber: Int = 0

    /**
     * Creates a new test that asserts that when given this string as input, we get the correct [expected] answer.
     * The [expected] value can be anything and will be tested against its String value.
     */
    public suspend infix fun String.shouldOutput(expected: Any) {
        exampleNumber++
        val input = PuzzleInput(this)
        val preview = input.preview()
        context.test("Example #$exampleNumber") {
            withClue("Expected answer '$expected' for input: $preview") {
                val answer = shouldNotThrowAny {
                    this@AdventSpecExampleContainerScope.implementation(input.toString()).toString()
                }
                answer shouldBe expected.toString()
                println("Correct answer '$expected' given for input: $preview")
            }
        }
    }

    /**
     * For each of the values given creates a new test that asserts that when given as input, we get the correct
     * [expected] answer.
     * The [expected] value can be anything and will be tested against its String value.
     */
    public suspend infix fun Iterable<String>.shouldAllOutput(expected: Any) {
        forEach { it shouldOutput expected }
    }
}
