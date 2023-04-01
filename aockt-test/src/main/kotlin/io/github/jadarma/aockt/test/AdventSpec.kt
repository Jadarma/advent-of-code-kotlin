package io.github.jadarma.aockt.test

import io.github.jadarma.aockt.core.Solution
import io.kotest.core.spec.style.FunSpec

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
 * class Y2015D01Test : AdventSpec(2015D01(), {
 *     // ...
 * }
 * ```
 */
public abstract class AdventSpec(
    protected val solution: Solution,
    body: AdventSpec.() -> Unit = {},
) : FunSpec() {
    init {
        body()
    }
}
