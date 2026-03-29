// Copyright © 2020 Dan Cîmpianu
// This code is licensed under the MIT license, detailed in LICENSE.md or at https://opensource.org/license/MIT.
package io.github.jadarma.aockt.internal

import io.github.jadarma.aockt.AdventPartScope

internal class AdventPartScopeImpl : AdventPartScope {

    private val examples = mutableListOf<Pair<PuzzleInput, PuzzleAnswer>>()
    val testCases: List<Pair<PuzzleInput, PuzzleAnswer>> get() = examples

    override infix fun String.shouldOutput(expected: Any) {
        examples.add(PuzzleInput(this) to PuzzleAnswer(expected.toString()))
    }

    override infix fun Iterable<String>.shouldAllOutput(expected: Any) {
        forEach { it shouldOutput expected }
    }
}
