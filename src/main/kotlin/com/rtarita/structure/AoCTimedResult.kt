package com.rtarita.structure

import com.rtarita.structure.fmt.Formatter
import com.rtarita.structure.fmt.GenericFormatter
import com.rtarita.structure.fmt.IndentConfig
import com.rtarita.structure.fmt.Indenter
import kotlin.time.Duration
import kotlin.time.DurationUnit

class AoCTimedResult(
    aocday: AoCDay,
    result1: Any?,
    result2: Any?,
    val timing1: Duration,
    val timing2: Duration,
    fmt1: Formatter<Any?> = GenericFormatter,
    fmt2: Formatter<Any?> = fmt1,
) : AoCResult(aocday, result1, result2, fmt1, fmt2) {
    private fun formatTiming(timing: Duration): String {
        val ms = timing.inWholeMilliseconds
        val msString = if (ms == 0L) "${timing.toDouble(DurationUnit.MILLISECONDS)}" else ms.toString()
        return "=== took ${msString}ms (${timing.toDouble(DurationUnit.SECONDS)}s) ==="
    }

    override fun formatResult(config: IndentConfig): String {
        val indenter = Indenter(config)
        return indenter.append("Day ${aocday.day.dayOfMonth}:")
            .increaseLevel()
            .append("Part 1:")
            .increaseLevel()
            .append(result1, fmt1)
            .append(formatTiming(timing1))
            .decreaseLevel()
            .append("Part 2:")
            .increaseLevel()
            .append(result2, fmt2)
            .append(formatTiming(timing2))
            .resetLevel()
            .getText()
    }
}