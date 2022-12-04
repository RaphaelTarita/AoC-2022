package com.rtarita.days

import com.rtarita.structure.AoCDay
import com.rtarita.util.day
import kotlinx.datetime.LocalDate

object Day3 : AoCDay {
    private const val LOWER_A = 'a'
    private const val UPPER_A = 'A'
    override val day: LocalDate = day(3)

    private fun splitInHalf(str: String): Pair<Set<Char>, Set<Char>> {
        return str.substring(0, str.length / 2).toSet() to str.substring(str.length / 2).toSet()
    }

    private fun Char.priority(): Int {
        return when {
            isLowerCase() -> code - LOWER_A.code + 1
            isUpperCase() -> code - UPPER_A.code + 27
            else -> error("invalid character: $this")
        }
    }

    override fun executePart1(input: String): Int {
        return input.lineSequence()
            .map { splitInHalf(it) }
            .map { (a, b) -> (a intersect b).single().priority() }
            .sum()
    }

    override fun executePart2(input: String): Int {
        return input.lineSequence()
            .chunked(3)
            .map { (a, b, c) -> (a.toSet() intersect b.toSet() intersect c.toSet()).single().priority() }
            .sum()
    }
}