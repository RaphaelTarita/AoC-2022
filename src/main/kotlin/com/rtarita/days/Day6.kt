package com.rtarita.days

import com.rtarita.structure.AoCDay
import com.rtarita.util.day
import kotlinx.datetime.LocalDate

object Day6 : AoCDay {
    override val day: LocalDate = day(6)

    private fun findFirstMarker(input: String, uniqueSize: Int): Int {
        return input.asSequence()
            .windowed(uniqueSize)
            .indexOfFirst { it.toSet().size == uniqueSize } + uniqueSize
    }

    override fun executePart1(input: String): Int {
        return findFirstMarker(input, 4)
    }

    override fun executePart2(input: String): Int {
        return findFirstMarker(input, 14)
    }
}