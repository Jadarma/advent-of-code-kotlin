package aoc.core

import aoc.core.internal.*
import aoc.core.internal.CLI
import aoc.core.internal.Logger
import aoc.core.internal.Runner
import aoc.core.internal.RunnerImpl

public class AdventOfCode(private val configuration: Configuration) {

    private val runner: Runner = RunnerImpl(configuration.repeat)
    private val logger: Logger = LoggerImpl(configuration.enableColor)

    public fun run(): Unit =
        AdventCalendar
            .select(configuration.daySelector)
            .asSequence()
            .map(runner::run)
            .forEach(logger::print)

    public companion object {
        public fun main(args: Array<String>): Unit = CLI().main(args)
    }
}
