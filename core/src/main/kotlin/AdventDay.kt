package aoc.core

import aoc.core.internal.AdventMetadata

/** The abstract base of an Advent of Code daily challenge. */
public abstract class AdventDay(year: Int, day: Int, title: String = "Untitled") : Comparable<AdventDay> {

    internal val metadata = AdventMetadata(year, day, title)

    /** Given an [input], calculates the solution to the first part of the problem. */
    public open fun partOne(input: String): Any =
        throw NotImplementedError("Part one of $this was not implemented.")

    /** Given an [input], calculates the solution to the second part of the problem. */
    public open fun partTwo(input: String): Any =
        throw NotImplementedError("part two of $this was not implemented or does not exist.")

    /** Guaranteed unique number as it is the YYYYDD format. */
    final override fun hashCode(): Int = metadata.year * 100 + metadata.day

    /** Days are sorted chronologically. */
    final override fun compareTo(other: AdventDay): Int = hashCode().compareTo(other.hashCode())

    /** Different instances cannot be equal to each-other because of possibly different implementations. */
    final override fun equals(other: Any?): Boolean = this === other

    /** Prints the day as an `Y<YYYY>D<DD>` format, e.g: `Y2015D01`. */
    final override fun toString(): String = metadata.toString()
}
