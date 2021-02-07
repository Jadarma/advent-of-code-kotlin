package aoc.solutions.y2016

import aoc.core.AdventDay
import java.security.MessageDigest

class Y2016D05 : AdventDay(2016, 5, "How About a Nice Game of Chess?") {

    /** Returns the hexadecimal value of this string's MD5 hash. */
    private val String.md5
        get() =
            MessageDigest
                .getInstance("MD5")
                .digest(this.toByteArray())
                .joinToString("") { "%02x".format(it) }

    /** Brute forces MD5 hashes that start with five zeroes, in ascending order. */
    private fun bruteForce(input: String) =
        generateSequence(0, Int::inc)
            .map { "$input$it".md5 }
            .filter { "$input$it".md5.startsWith("00000") }

    override fun partOne(input: String) =
        bruteForce(input)
            .take(8)
            .map { it[5] }
            .joinToString("")

    override fun partTwo(input: String): String {
        val password = CharArray(8) { '_' }

        bruteForce(input)
            .map { (it[5] - '0') to it[6] }
            .filter { it.first in 0..7 }
            .filter { password[it.first] == '_' }
            .onEach { password[it.first] = it.second }
            .takeWhile { password.any { it == '_' } }
            .count()

        return String(password)
    }
}
