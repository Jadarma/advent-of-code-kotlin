package io.github.jadarma.aockt.test

import io.github.jadarma.aockt.core.Solution
import io.github.jadarma.aockt.test.internal.AdventRootScopeImpl
import io.github.jadarma.aockt.test.internal.PuzzleTestData
import io.github.jadarma.aockt.test.internal.TestData
import io.github.jadarma.aockt.test.internal.adventDay
import io.github.jadarma.aockt.test.internal.id
import io.github.jadarma.aockt.test.internal.registerDebug
import io.github.jadarma.aockt.test.internal.registerTest
import io.kotest.common.ExperimentalKotest
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.FunSpec
import io.kotest.core.test.TestCaseOrder
import io.kotest.engine.concurrency.TestExecutionMode
import io.kotest.engine.coroutines.CoroutineDispatcherFactory
import io.kotest.engine.coroutines.ThreadPerSpecCoroutineContextFactory

/**
 * A [FunSpec] specialized for testing Advent of Code puzzle [Solution]s.
 * The test classes extending this should also provide information about the puzzle with an [AdventDay] annotation.
 *
 * Example:
 * ```kotlin
 * import io.github.jadarma.aockt.core.Solution
 * import io.github.jadarma.aockt.test.AdventSpec
 * import io.github.jadarma.aockt.test.AdventDay
 *
 * @AdventDay(2015, 1, "Not Quite Lisp")
 * class Y2015D01Test : AdventSpec<Y2015D01>({
 *     partOne {
 *         listOf("(())", "()()") shouldAllOutput 0
 *         listOf("(((", "(()(()(") shouldAllOutput 3
 *         listOf("())", "))(") shouldAllOutput -1
 *         listOf(")))", ")())())") shouldAllOutput -3
 *     }
 *     partTwo {
 *         ")" shouldOutput 1
 *         "()())" shouldOutput 5
 *     }
 * }
 * ```
 *
 * @param T    The implementation class of the [Solution] to be tested.
 * @param body A context in which to configure the tests.
 */
@Suppress("AbstractClassCanBeConcreteClass")
@OptIn(ExperimentalKotest::class)
public abstract class AdventSpec<T : Solution>(
    body: AdventRootScope.() -> Unit = {},
) : FunSpec() {

    internal val testData: PuzzleTestData = TestData.inputFor(this::class.adventDay.id)

    init {
        AdventRootScopeImpl(owner = this::class).apply {
            body()
            partOne?.let(::registerTest)
            partTwo?.let(::registerTest)
            debug?.let(::registerDebug)
        }
    }

    // Enforce some configuration to ensure that all tests within one AdventSpec will be executed sequentially on a
    // single thread.
    final override fun coroutineDispatcherFactory(): CoroutineDispatcherFactory = ThreadPerSpecCoroutineContextFactory
    final override fun isolationMode(): IsolationMode = IsolationMode.SingleInstance
    final override fun testCaseOrder(): TestCaseOrder = TestCaseOrder.Sequential
    final override fun testExecutionMode(): TestExecutionMode = TestExecutionMode.Sequential
}
