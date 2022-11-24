package com.rtarita.days

import com.rtarita.structure.AoCDay
import com.rtarita.util.day
import kotlinx.datetime.LocalDate

object Day0 : AoCDay {
    override val day: LocalDate = day(0)

    override fun executePart1(input: String): Any {
        return "Test day part 1 executed successfully (this is Day \"0\", which is intended either for testing the framework or as a fallback)"
    }

    override fun executePart2(input: String): Any {
        return "Test day part 2 executed successfully (this is Day \"0\", which is intended either for testing the framework or as a fallback)"
    }
}