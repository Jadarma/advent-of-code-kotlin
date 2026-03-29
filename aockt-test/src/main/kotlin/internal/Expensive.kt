// Copyright © 2020 Dan Cîmpianu
// This code is licensed under the MIT license, detailed in LICENSE.md or at https://opensource.org/license/MIT.
package io.github.jadarma.aockt.internal

import io.kotest.core.Tag

/**
 * A tag that marks advent tests as containing expensive computations.
 * Applied when setting the `expensive` parameter of advent parts to `true`.
 */
internal object Expensive : Tag()
