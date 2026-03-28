// Copyright © 2020 Dan Cîmpianu
// This code is licensed under the MIT license, detailed in LICENSE.md or at https://opensource.org/license/MIT.
package io.github.jadarma.aockt.internal

import io.github.jadarma.aockt.AdventDay
import io.github.jadarma.aockt.AdventSpec
import io.kotest.core.test.TestCase
import io.kotest.engine.names.DisplayNameFormatter
import kotlin.reflect.KClass
import kotlin.reflect.full.isSubclassOf

/** A name formatter extension that adjusts the names of [AdventSpec]s with the info of their [AdventDay]. */
internal object AocKtDisplayNameFormatter : DisplayNameFormatter {

    // Test cases are not formatted.
    override fun format(testCase: TestCase) = null

    override fun format(kclass: KClass<*>): String? {
        if (!kclass.isSubclassOf(AdventSpec::class)) return null

        @Suppress("UNCHECKED_CAST")
        return with((kclass as KClass<out AdventSpec<*>>).adventDay) {
            buildString {
                append(AdventDayID(year, day))
                if (title.isNotEmpty()) append(": ", title)
                if (variant.isNotEmpty()) append(" (", variant, ")")
            }
        }
    }
}
