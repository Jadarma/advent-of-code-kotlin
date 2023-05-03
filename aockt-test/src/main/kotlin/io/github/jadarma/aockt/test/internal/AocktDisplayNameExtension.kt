package io.github.jadarma.aockt.test.internal

import io.github.jadarma.aockt.test.AdventDay
import io.github.jadarma.aockt.test.AdventSpec
import io.kotest.core.extensions.DisplayNameFormatterExtension
import io.kotest.core.extensions.Extension
import io.kotest.core.names.DisplayNameFormatter
import io.kotest.core.project.ProjectContext
import io.kotest.core.test.TestCase
import io.kotest.engine.test.names.getDisplayNameFormatter
import io.kotest.mpp.annotation
import kotlin.reflect.KClass
import kotlin.reflect.full.isSubclassOf

/** A name formatter extension that adjusts the names of [AdventSpec]s with the info of their [AdventDay]. */
internal class AocktDisplayNameExtension(
    private val fallbackFormatter: DisplayNameFormatter,
) : DisplayNameFormatter, DisplayNameFormatterExtension, Extension {

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

    override fun formatter(): DisplayNameFormatter = this
}

internal fun ProjectContext.configureAocKtDisplayNameExtension() {
    val fallbackFormatter = getDisplayNameFormatter(configuration.registry, configuration)

    configuration.registry.all()
        .filterIsInstance<DisplayNameFormatterExtension>()
        .filterIsInstance<Extension>()
        .forEach(configuration.registry::remove)

    configuration.registry.add(AocktDisplayNameExtension(fallbackFormatter))
}
