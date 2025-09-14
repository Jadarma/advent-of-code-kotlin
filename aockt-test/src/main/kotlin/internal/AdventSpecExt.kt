package io.github.jadarma.aockt.test.internal

import io.github.jadarma.aockt.core.Solution
import io.github.jadarma.aockt.test.AdventSpec
import io.kotest.common.reflection.ReflectionInstantiations.newInstanceNoArgConstructorOrObjectInstance
import kotlin.reflect.KClass
import kotlin.reflect.full.isSubtypeOf
import kotlin.reflect.full.starProjectedType
import kotlin.reflect.jvm.jvmErasure
import kotlin.reflect.typeOf

/**
 * Construct and inject a solution instance for this [AdventSpec].
 * This function is designed to be called at spec instantiation time.
 * Uses reflection magic that while not that pretty, is fine for use in unit tests, and allows for a more elegant syntax
 * when declaring specs.
 */
@Suppress("UNCHECKED_CAST", "UnsafeCallOnNullableType")
internal fun AdventSpec<*>.injectSolution(): Solution = this::class
    .starProjectedType.jvmErasure.supertypes
    .first { it.isSubtypeOf(typeOf<AdventSpec<*>>()) }
    .arguments.first().type!!.jvmErasure
    .run {
        this as KClass<Solution> // Must be a solution because of AdventSpec bounds.
        runCatching { newInstanceNoArgConstructorOrObjectInstance(this) }
            .getOrElse { throw MissingNoArgConstructorException(this) }
    }
