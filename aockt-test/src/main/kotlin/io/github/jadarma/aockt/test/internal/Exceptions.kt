package io.github.jadarma.aockt.test.internal

import io.github.jadarma.aockt.test.AdventSpec
import io.github.jadarma.aockt.test.AdventDay
import kotlin.reflect.KClass

/** Base [Exception] type for all exceptions related to AocKt. */
@Suppress("UnnecessaryAbstractClass")
internal abstract class AocktException(message: String? = null, cause: Throwable? = null) : Exception(message, cause)

/**
 * An [AdventSpec] was declared without an [AdventDay] annotation.
 * It is required in order to determine test input.
 */
internal class MissingAdventDayAnnotationException(kclass: KClass<*>) : AocktException(
    message = "Class ${kclass.qualifiedName} is an AdventSpec but is missing the AdventDay annotation."
)

/**
 * An [AdventSpec] defined a part that was configured incorrectly, specifying a conflict in how to handle the example
 * cases.
 */
internal class ConflictingPartExampleConfigurationException(spec: KClass<*>) : AocktException(
    message = "In ${spec.qualifiedName}, a part can either be configured to execute only the examples, or skip them," +
        " but not both."
)
