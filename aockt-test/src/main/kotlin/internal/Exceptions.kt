package io.github.jadarma.aockt.test.internal

import io.github.jadarma.aockt.core.Solution
import io.github.jadarma.aockt.test.AdventSpec
import io.github.jadarma.aockt.test.AdventDay
import io.kotest.common.reflection.bestName
import kotlin.reflect.KClass

/** Base [Exception] type for all exceptions related to AocKt. */
internal sealed class AocktException(message: String? = null, cause: Throwable? = null) : Exception(message, cause)

/** A general exception thrown upon a misconfiguration event. */
internal class ConfigurationException(message: String? = null) : AocktException(message = message)

/**
 * An [AdventSpec] was declared without an [AdventDay] annotation.
 * It is required in order to determine test input.
 */
internal class MissingAdventDayAnnotationException(kclass: KClass<out AdventSpec<*>>) : AocktException(
    message = "Class ${kclass.bestName()} is an AdventSpec but is missing the AdventDay annotation.",
)

/**
 * While creating an [AdventSpec], the [Solution] to be tested is a type that does not provide a no-arg constructor,
 * which is required since [Solution]s should not be stateful.
 * Add a no-arg constructor to it or declare it as an object.
 */
internal class MissingNoArgConstructorException(kclass: KClass<out Solution>) : AocktException(
    message = "Class ${kclass.bestName()} is a Solution but it is missing a no-arg constructor.",
)

/**
 * An [AdventSpec] declared the same part scope twice.
 * The [AdventSpec.partOne] and [AdventSpec.partTwo] should be used at most once per spec.
 */
internal class DuplicatePartDefinitionException(spec: KClass<*>, part: AdventDayPart) : AocktException(
    message = "In ${spec.bestName()}, part$part has been declared twice.",
)
