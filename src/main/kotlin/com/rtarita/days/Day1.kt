package com.rtarita.days

import com.rtarita.structure.AoCDay
import com.rtarita.util.day
import kotlinx.datetime.LocalDate

object Day1 : AoCDay {
    override val day: LocalDate = day(1)

    private fun getSums(input: String): Sequence<Int> {
        return input.splitToSequence("\n\n")
            .map { elf ->
                elf.lineSequence()
                    .sumOf { it.toInt() }
            }
    }

    override fun executePart1(input: String): Int {
        return getSums(input).max()
    }

    override fun executePart2(input: String): Int {
        return getSums(input).sortedDescending()
            .take(3)
            .sum()
    }
}