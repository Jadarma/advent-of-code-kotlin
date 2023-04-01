package io.github.jadarma.aockt.test

import io.github.jadarma.aockt.test.internal.AdventDayID
import io.kotest.core.extensions.DisplayNameFormatterExtension
import io.kotest.core.extensions.Extension
import io.kotest.core.names.DisplayNameFormatter
import io.kotest.core.test.TestCase
import io.kotest.mpp.annotation
import kotlin.reflect.KClass
import kotlin.reflect.full.isSubclassOf

/**
 * A name formatter extension that adjusts the names of [AdventSpec]s with the info of their [AdventDay].
 * Should be loaded in the project configuration.
 *
 * Example:
 * ```kotlin
 * object TestConfig : AbstractProjectConfig() {
 *     override fun extensions() = listOf(
 *         AocktDisplayNameExtension(DefaultDisplayNameFormatter())
 *     )
 * }
 * ```
 */
public class AocktDisplayNameExtension(
    private val fallbackFormatter: DisplayNameFormatter,
) : DisplayNameFormatterExtension, Extension {

    override fun formatter(): DisplayNameFormatter = object : DisplayNameFormatter {

        override fun format(testCase: TestCase): String = fallbackFormatter.format(testCase)

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

        override fun toString(): String = "AocktDisplayNameFormatter"
    }
}
