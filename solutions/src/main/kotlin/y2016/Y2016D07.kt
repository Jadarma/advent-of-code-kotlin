package aoc.solutions.y2016

import aoc.core.AdventDay

@ExperimentalStdlibApi
class Y2016D07 : AdventDay(2016, 7, "Internet Protocol Version 7") {

    /** A valid IPv7 address. */
    private data class IPv7Address(val value: String) {

        /** All sequences in the address, in order. */
        val sequences: List<String> = buildList {
            // Note: This initialization is just for show, correctly parsing the addresses and reporting errors.
            // However, since the problem input is always valid, this can be replaced with just for brevity:
            //
            // ```kotlin
            // val sequences = value.split(Regex("""[\[\]]"""))
            // ```
            val sequence = StringBuilder()
            var insideSequence = false
            val emitSequence = {
                val net = sequence.toString()
                sequence.clear()
                require(net.isNotEmpty()) { "Invalid address; sequences cannot be empty." }
                add(net)
            }
            value.forEach { c ->
                when (c) {
                    '[' -> {
                        require(!insideSequence) { "Invalid address; sequences cannot be nested." }
                        insideSequence = true
                        emitSequence()
                    }
                    ']' -> {
                        require(insideSequence) { "Invalid address; unmatched closing bracket." }
                        insideSequence = false
                        emitSequence()
                    }
                    else -> {
                        require(c in 'a'..'z') { "Invalid address; illegal character." }
                        sequence.append(c)
                    }
                }
            }
            require(!insideSequence) { "Invalid address; unmatched opening bracket." }
            emitSequence()
        }

        /** Returns only the superNet sequences of the address (outside of brackets). */
        val superNetSequences: List<String> get() = sequences.filterIndexed { index, _ -> index % 2 == 0 }

        /** Returns only the hyperNet sequences of the address (inside of brackets). */
        val hyperNetSequences: List<String> get() = sequences.filterIndexed { index, _ -> index % 2 == 1 }

        /** Whether this address supports Transport-Layer Snooping. */
        val supportsTls: Boolean
            get() = superNetSequences.any { it.findABBAs().isNotEmpty() } &&
                    hyperNetSequences.all { it.findABBAs().isEmpty() }

        /** Whether this address supports Super-Secret Listening. */
        val supportsSsl: Boolean
            get() {
                val aba = superNetSequences.flatMap { it.findABAs() }.toSet()
                val bab = hyperNetSequences.flatMap { it.findABAs() }.toSet()
                return aba.any { a -> bab.any { b -> a[0] == b[1] && a[1] == b[0] } }
            }

        /** Returns a set of all Autonomous Bridge Bypass Annotation contained in this sequence. */
        private fun String.findABBAs() =
            windowedSequence(4)
                .filter { it[0] != it[1] && it[0] == it[3] && it[1] == it[2] }
                .toSet()

        /** Returns a set of all Area-Broadcast Accessors and Byte Allocation Blocks contained in this sequence. */
        private fun String.findABAs(): Set<String> =
            windowedSequence(3)
                .filter { it[0] != it[1] && it[0] == it[2] }
                .toSet()
    }

    override fun partOne(input: String) = input.lineSequence().map(::IPv7Address).count { it.supportsTls }
    override fun partTwo(input: String) = input.lineSequence().map(::IPv7Address).count { it.supportsSsl }
}
