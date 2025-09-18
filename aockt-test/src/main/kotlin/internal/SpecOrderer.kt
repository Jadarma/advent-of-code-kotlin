package io.github.jadarma.aockt.test.internal

import io.github.jadarma.aockt.test.AdventDay
import io.github.jadarma.aockt.test.AdventSpec
import io.kotest.core.spec.Spec
import io.kotest.core.spec.SpecRef
import kotlin.reflect.KClass
import kotlin.reflect.full.isSubclassOf

/**
 * Defines the execution order of [SpecRef]s.
 * All non-[AdventSpec] are executed first in the order they were discovered.
 * Advent specs are then executed in chronological order.
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
                a = (specA as KClass<out AdventSpec<*>>).adventDay,
                b = (specB as KClass<out AdventSpec<*>>).adventDay,
                AdventDay::year,
                AdventDay::day,
                AdventDay::title,
                AdventDay::variant,
            )
        }
    }
}
