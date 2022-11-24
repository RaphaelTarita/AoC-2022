package com.rtarita.structure

import com.rtarita.structure.fmt.Formatter
import com.rtarita.structure.fmt.GenericFormatter
import kotlinx.datetime.LocalDate

interface AoCDay {
    val day: LocalDate
    fun executePart1(input: String): Any?
    fun executePart2(input: String): Any?

    val outputFormatter1: Formatter<Any?>
        get() = GenericFormatter
    val outputFormatter2: Formatter<Any?>
        get() = GenericFormatter
}