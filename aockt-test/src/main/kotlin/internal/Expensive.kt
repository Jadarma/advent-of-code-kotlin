// Copyright © 2020 Dan Cîmpianu
// This code is licensed under the MIT license, detailed in LICENSE.md or at https://opensource.org/license/MIT.
package io.github.jadarma.aockt.test.internal

import io.kotest.core.Tag

/**
 * A tag that marks a spec or test as containing expensive computations.
 *
 * Useful for marking the test specs for some days (like brute force challenges) so they can be excluded conditionally
 * in bulk test executions.
 */
internal object Expensive : Tag()
