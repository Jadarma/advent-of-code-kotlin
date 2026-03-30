// Copyright © 2020 Dan Cîmpianu
// This code is licensed under the MIT license, detailed in LICENSE.md or at https://opensource.org/license/MIT.
package io.github.jadarma.aockt.internal

import io.github.jadarma.aockt.AdventDay
import io.github.jadarma.aockt.AdventSpec
import io.kotest.core.spec.Spec
import io.kotest.core.spec.SpecRef
import kotlin.reflect.KClass
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.isSubclassOf

/**
 * Defines the execution order of [SpecRef]s:
 * - All non-[AdventSpec]s executed first, in discovery order.
 * - All [AdventSpec]s missing their [AdventDay] annotation are executed next, in discovery order _(they will fail!)_.
 * - All remaining [AdventSpec]s in chronological order.
 */
internal object SpecOrderer : Comparator<SpecRef> {

    override fun compare(a: SpecRef, b: SpecRef): Int {
        val specA: KClass<out Spec> = a.kclass
        val specB: KClass<out Spec> = b.kclass

        @Suppress("UNCHECKED_CAST")
        return when (specA.isSubclassOf(AdventSpec::class) to specB.isSubclassOf(AdventSpec::class)) {
            true to false -> 1
            false to true -> -1
            false to false -> 0
            else -> compareValuesBy(
                a = (specA as KClass<out AdventSpec<*>>).findAnnotation<AdventDay>(),
                b = (specB as KClass<out AdventSpec<*>>).findAnnotation<AdventDay>(),
                { it?.year },
                { it?.day },
                { it?.title },
                { it?.variant },
            )
        }
    }
}
