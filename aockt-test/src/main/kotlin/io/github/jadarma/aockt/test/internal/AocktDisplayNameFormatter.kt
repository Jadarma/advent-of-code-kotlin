package io.github.jadarma.aockt.test.internal

import io.github.jadarma.aockt.test.AdventDay
import io.github.jadarma.aockt.test.AdventSpec
import io.kotest.core.names.DisplayNameFormatter
import io.kotest.engine.test.names.DefaultDisplayNameFormatter
import io.kotest.mpp.annotation
import kotlin.reflect.KClass
import kotlin.reflect.full.isSubclassOf

/**
 * A name formatter extension that adjusts the names of [AdventSpec]s with the info of their [AdventDay].
 * @property fallbackFormatter The formatter to use for test cases and non-advent specs.
 */
internal class AocktDisplayNameFormatter(
    private val fallbackFormatter: DisplayNameFormatter = DefaultDisplayNameFormatter(),
) : DisplayNameFormatter by fallbackFormatter {

    override fun format(kclass: KClass<*>): String {
        val annotation = kclass.annotation<AdventDay>()

        if (annotation == null || !kclass.isSubclassOf(AdventSpec::class)) {
            return fallbackFormatter.format(kclass)
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
