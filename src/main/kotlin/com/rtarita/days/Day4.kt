package com.rtarita.days

import com.rtarita.structure.AoCDay
import com.rtarita.util.containsAll
import com.rtarita.util.day
import com.rtarita.util.intersect
import com.rtarita.util.isNotEmpty
import kotlinx.datetime.LocalDate

object Day4 : AoCDay {
    override val day: LocalDate = day(4)

    private fun parseRanges(line: String): Pair<IntRange, IntRange> {
        val (first, second) = line.split(',')
        val (firstStart, firstEnd) = first.split('-')
        val (secondStart, secondEnd) = second.split('-')
        return firstStart.toInt()..firstEnd.toInt() to secondStart.toInt()..secondEnd.toInt()
    }

    override fun executePart1(input: String): Int {
        return input.lineSequence()
            .map { parseRanges(it) }
            .count { (a, b) -> a.containsAll(b) || b.containsAll(a) }

    }

    override fun executePart2(input: String): Int {
        return input.lineSequence()
            .map { parseRanges(it) }
            .count { (a, b) -> (a intersect b).isNotEmpty() }
    }
}