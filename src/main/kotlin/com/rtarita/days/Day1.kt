package com.rtarita.days

import com.rtarita.structure.AoCDay
import com.rtarita.util.day
import kotlinx.datetime.LocalDate

object Day1 : AoCDay {
    override val day: LocalDate = day(1)

    override fun executePart1(input: String): Any {
        return input.splitToSequence("\n\n")
            .maxOf { elf ->
                elf.lineSequence()
                    .sumOf { it.toInt() }
            }
    }

    override fun executePart2(input: String): Any {
        return input.splitToSequence("\n\n")
            .map { elf ->
                elf.lineSequence()
                    .sumOf { it.toInt() }
            }
            .sortedDescending()
            .take(3)
            .sum()
    }
}