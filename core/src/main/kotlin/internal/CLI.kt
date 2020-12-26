package aoc.core.internal

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.groups.default
import com.github.ajalt.clikt.parameters.groups.mutuallyExclusiveOptions
import com.github.ajalt.clikt.parameters.options.*
import com.github.ajalt.clikt.parameters.types.int
import aoc.core.AdventOfCode
import aoc.core.Configuration
import aoc.core.DaySelector
import aoc.core.EnableColor
import com.github.ajalt.clikt.parameters.groups.single
import com.github.ajalt.clikt.parameters.types.enum

internal class CLI : CliktCommand(name = "aoc-cli") {

    private val selector: DaySelector by mutuallyExclusiveOptions(
        option(help = "Runs all the solutions available.")
            .switch("-a" to DaySelector.All, "--all" to DaySelector.All),
        option("-y", "--year", help = "Runs all solutions in a specific year. (e.g. -y 2015)")
            .int()
            .convert { DaySelector.Year(it) },
        option("-d", "--day", help = "Runs only the solution for a specific day (e.g. -d 2015 1)")
            .int()
            .transformValues(2) { DaySelector.Single(it[0], it[1]) },
    ).single().default(DaySelector.Last)

    private val repeat by option("-n", "--repeat", help = "Execute each part multiple times. Only once by default.")
        .int()
        .default(1)
        .validate { require(it > 0) { "Value must be positive." } }

    private val color: EnableColor by option("--color", help = "When to enable colored output.")
        .enum<EnableColor>()
        .default(EnableColor.AUTO)

    override val commandHelp: String =
        """
        Runs the advent of code solutions present in your project.
        If no day selector is passed (--all, --day, or --year) only the last chronological advent day will be ran.
        """

    override fun run() {
        val configuration = Configuration(selector, repeat, color)
        AdventOfCode(configuration).run()
    }
}
