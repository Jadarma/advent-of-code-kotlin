package io.github.jadarma.aockt.test.integration.sample

import io.github.jadarma.aockt.core.Solution
import io.github.jadarma.aockt.test.AdventSpec

@Suppress("AbstractClassCanBeInterface")
abstract class SampleSpec<T : Solution> : AdventSpec<T>({
    partOne {
        "hello" shouldOutput "1:hello"
        "pass" shouldOutput "1:pass"
    }
    partTwo {
        "hello" shouldOutput "2:5"
        "pass" shouldOutput "2:4"
    }
})
