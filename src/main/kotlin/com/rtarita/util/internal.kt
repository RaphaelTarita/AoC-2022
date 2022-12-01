package com.rtarita.util

import com.rtarita.structure.AoCDay
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn
import java.io.File
import java.nio.file.Path

fun day(num: Int): LocalDate {
    return if (num == 0) {
        LocalDate(1970, 1, 1)
    } else {
        return LocalDate(2022, Month.DECEMBER, num)
    }
}

fun today(): LocalDate = Clock.System.todayIn(TimeZone.EST)

val TimeZone.Companion.EST: TimeZone
    get() = of("UTC-5")

val AoCDay.mapPair: Pair<LocalDate, AoCDay>
    get() = day to this

val AoCDay.inputPath: Path
    get() = pathOfDay(day)

val AoCDay.inputFile: File
    get() = fileOfDay(day)