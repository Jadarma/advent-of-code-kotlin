package io.github.jadarma.aockt.test.internal

import io.github.jadarma.aockt.test.AdventDay
import io.github.jadarma.aockt.test.AdventSpec
import io.kotest.core.names.DisplayNameFormatter
import io.kotest.core.test.TestCase
import io.kotest.mpp.annotation
import kotlin.reflect.KClass
import kotlin.reflect.full.isSubclassOf

/** A name formatter extension that adjusts the names of [AdventSpec]s with the info of their [AdventDay]. */
internal class AocktDisplayNameFormatter(private val disabled: Boolean = false) : DisplayNameFormatter {

    // Test cases are not formatted.
    override fun format(testCase: TestCase) = null

    @Suppress("ReturnCount")
    override fun format(kclass: KClass<*>): String? {
        if (disabled) return null

        val annotation = kclass.annotation<AdventDay>()

        if (annotation == null || !kclass.isSubclassOf(AdventSpec::class)) {
            return null
        }

        return buildString {
            append(AdventDayID(annotation.year, annotation.day))
            if (annotation.title.isNotEmpty()) {
                append(": ")
                append(annotation.title)
            }
            if (annotation.variant != "default") {
                append(" (")
                append(annotation.variant)
                append(')')
            }
        }
    }
}
