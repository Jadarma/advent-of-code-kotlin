package io.github.jadarma.aockt.test

import io.kotest.core.Tag

/**
 * A tag that marks a spec or test as containing expensive computations.
 *
 * Useful for marking the test specs for some days (like brute force challenges) so they can be excluded conditionally
 * in bulk test executions.
 */
public object ExpensiveDay : Tag()
