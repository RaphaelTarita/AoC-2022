package com.rtarita.days

import com.rtarita.structure.AoCDay
import com.rtarita.util.day
import com.rtarita.util.splitTopLevel
import kotlinx.datetime.LocalDate

object Day13 : AoCDay {
    override val day: LocalDate = day(13)

    private fun parseList(list: String): List<*> {
        if (list == "[]") return emptyList<Any?>()
        return list.substring(1 until list.lastIndex)
            .splitTopLevel()
            .map { it.toIntOrNull() ?: parseList(it) }
    }

    private fun getListPairs(input: String): List<Pair<List<*>, List<*>>> {
        return input.split("\n\n")
            .map { it.lines() }
            .map { (a, b) -> parseList(a) to parseList(b) }
    }

    private fun compare(left: List<*>, right: List<*>): Int {
        for ((i, lElem) in left.withIndex()) {
            if (right.lastIndex < i) return 1
            val comparison = when (lElem) {
                is Int -> when (val rElem = right[i]) {
                    is Int -> lElem - rElem
                    is List<*> -> compare(listOf(lElem), rElem)
                    else -> error("invalid type for element $rElem")
                }

                is List<*> -> when (val rElem = right[i]) {
                    is Int -> compare(lElem, listOf(rElem))
                    is List<*> -> compare(lElem, rElem)
                    else -> error("invalid type for element $rElem")
                }

                else -> error("invalid type for element $lElem")
            }
            if (comparison != 0) return comparison
        }
        return left.size - right.size
    }

    override fun executePart1(input: String): Int {
        return getListPairs(input).withIndex()
            .filter { (_, pair) -> compare(pair.first, pair.second) < 0 }
            .sumOf { (idx, _) -> idx + 1 }
    }

    override fun executePart2(input: String): Int {
        val divider1 = listOf(listOf(2))
        val divider2 = listOf(listOf(6))
        val sorted = getListPairs(input).flatMap { (a, b) -> listOf(a, b) }
            .plus(listOf(divider1, divider2))
            .sortedWith { a, b -> compare(a, b) }
        return (sorted.indexOfFirst { compare(it, divider1) == 0 } + 1) * (sorted.indexOfFirst { compare(it, divider2) == 0 } + 1)
    }
}