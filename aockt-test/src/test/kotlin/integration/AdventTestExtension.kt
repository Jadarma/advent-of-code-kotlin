package io.github.jadarma.aockt.integration

import io.github.jadarma.aockt.AdventSpec
import io.kotest.assertions.fail
import io.kotest.common.KotestInternal
import io.kotest.core.descriptors.DescriptorPaths
import io.kotest.core.extensions.TestCaseExtension
import io.kotest.core.listeners.AfterSpecListener
import io.kotest.core.spec.Spec
import io.kotest.core.test.TestCase
import io.kotest.core.test.TestType
import io.kotest.engine.test.TestResult
import kotlin.time.Duration

/**
 * Extension that validates that the registered tests and their outcomes exactly match the supplied specification.
 *
 * @param expectedTests Pairs of test descriptors to type of expected output.
 *                      Descriptors are the names of tests _(excluding trailing parens)_, separated by ` -- ` for each
 *                      depth level, e.g.: `Part One -- The solution -- Is correct`.
 *                      Output types are one of: `Error`, `Failure`, or `Success`.
 */
class AdventTestExtension(
    private val expectedTests: Map<String, String>
) : TestCaseExtension, AfterSpecListener {

    private val actualTests = mutableMapOf<String, String>()

    init {
        expectedTests.values.forEach {
            require(it in validOutcomes) { "Invalid expected outcome: $it" }
        }
    }

    override suspend fun intercept(
        testCase: TestCase,
        execute: suspend (TestCase) -> TestResult
    ): TestResult {
        val actualResult = execute(testCase)
        if (testCase.type == TestType.Container) return actualResult

        val descriptor = testCase.descriptorPath

        val expectedResult = expectedTests[descriptor] ?: return TestResult.Failure(
            duration = Duration.ZERO,
            cause = AssertionError("Unexpected test registered: $descriptor"),
        )

        actualTests[descriptor] = actualResult.name

        return if (expectedResult == actualResult.name) {
            TestResult.Success(actualResult.duration)
        } else {
            TestResult.Failure(
                duration = Duration.ZERO,
                cause = AssertionError("Expected $descriptor to produce $expectedResult but got ${actualResult.name}"),
            )
        }
    }

    override suspend fun afterSpec(spec: Spec) {
        val missingTests = expectedTests.keys - actualTests.keys
        if(missingTests.isEmpty()) return
        fail("Expected tests were not registered: ${missingTests.joinToString()}")
    }

    private companion object {

        val validOutcomes = setOf("Error", "Failure", "Success")

        /**
         * Gets the rendered descriptor path of the test, removing the spec ID and any output hints, like the actual
         * execution time, or unverified answer value.
         */
        @OptIn(KotestInternal::class)
        val TestCase.descriptorPath: String
            get() = descriptor.path().value
                .substringAfter(DescriptorPaths.SPEC_DELIMITER)
                .replace(Regex("""\s\(.+\)$"""), "")
    }
}

/** Convenience function to register expected tests for an [AdventSpec]. */
fun AdventSpec<*>.testExtension(vararg expectedTests: Pair<String, String>) {
    extension(AdventTestExtension(expectedTests.toMap()))
}
