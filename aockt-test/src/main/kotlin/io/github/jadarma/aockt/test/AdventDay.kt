package io.github.jadarma.aockt.test

/**
 * Marks an [AdventSpec] as being the tests of a solution to a specific advent puzzle.
 *
 * @property year The year this puzzle appeared in.
 * @property day The day this puzzle appeared in.
 * @property title The title of the puzzle. If unspecified will default to the date.
 * @property variant Serves as disambiguation if the project contains multiple solutions for the same day.
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
public annotation class AdventDay(
    val year: Int,
    val day: Int,
    val title: String = "",
    val variant: String = "default",
)
