package aoc.core.internal

import aoc.core.EnableColor
import com.github.ajalt.mordant.rendering.*
import com.github.ajalt.mordant.rendering.OverflowWrap.BREAK_WORD
import com.github.ajalt.mordant.rendering.TextAlign.CENTER
import com.github.ajalt.mordant.rendering.TextColors.*
import com.github.ajalt.mordant.rendering.TextStyles.bold
import com.github.ajalt.mordant.rendering.TextStyles.underline
import com.github.ajalt.mordant.table.ColumnWidth.Expand
import com.github.ajalt.mordant.table.ColumnWidth.Fixed
import com.github.ajalt.mordant.table.SectionBuilder
import com.github.ajalt.mordant.table.table
import com.github.ajalt.mordant.terminal.Terminal

/** Formats and pretty prints the outcomes of executing advent days. */
internal interface Logger {
    fun print(result: RunResult)
}

internal class LoggerImpl(colorMode: EnableColor) : Logger {

    private val terminal = Terminal(
        ansiLevel = when (colorMode) {
            EnableColor.AUTO -> null
            EnableColor.ALWAYS -> AnsiLevel.ANSI16
            EnableColor.NEVER -> AnsiLevel.NONE
        }
    )

    override fun print(result: RunResult) = table {
        borderStyle = BorderStyle.SQUARE_DOUBLE_SECTION_SEPARATOR
        borderTextStyle = gray
        outerBorder = false

        column(0) { width = Fixed(10) }
        column(1) { width = Fixed(8) }
        column(2) { width = Expand(1); overflowWrap = BREAK_WORD }
        column(3) { width = Fixed(15) }

        header {
            row {
                cell(bold(result.metadata.toString()))
                cell(bold(result.metadata.title)) {
                    align = CENTER
                    columnSpan = 3
                }
            }
            row("Part", "Result", "ResultData", "Average Time")
        }
        body {
            format(result.partOneResult, "Part 1")
            format(result.partTwoResult, "Part 2")
        }
        captionBottom(gray("@see: " + underline(result.metadata.url)), align = TextAlign.RIGHT)

    }.let { terminal.println(it); terminal.println() }

    private fun SectionBuilder.format(result: PartResult, label: String): Unit = when (result) {
        PartResult.NotImplemented -> row(label, gray("n/a"), gray("n/a"), gray("n/a"))
        is PartResult.Error -> row(label, red("Fail"), red(result.cause.toString()), gray("n/a"))
        is PartResult.Inconsistent -> row(
            label,
            (bold + yellow)("Warn"),
            yellow(result.results
                .groupBy { it }
                .asSequence()
                .map { "${it.key} (x${it.value.count()})" }
                .joinToString()
            ),
            gray("n/a")
        )
        is PartResult.Ok -> row(
            label,
            green("Pass"),
            green(result.result.toString()),
            blue(result.averageTime.toString())
        )
    }
}
