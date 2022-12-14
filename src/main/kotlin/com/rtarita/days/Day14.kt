package com.rtarita.days

import com.rtarita.structure.AoCDay
import com.rtarita.util.day
import com.rtarita.util.towards
import kotlinx.datetime.LocalDate

object Day14 : AoCDay {
    private data class Coord(val x: Int, val y: Int)

    override val day: LocalDate = day(14)
    private fun getRockGrid(input: String): Pair<MutableMap<Coord, Boolean>, Int> {
        val result = hashMapOf<Coord, Boolean>()
        input.lineSequence()
            .map { it.splitToSequence(" -> ") }
            .flatMap { path ->
                path.map { it.split(',') }
                    .map { (x, y) -> Coord(x.toInt(), y.toInt()) }
                    .zipWithNext()
            }.flatMap { (start, end) ->
                when {
                    start.y == end.y -> (start.x towards end.x).map { Coord(it, start.y) }
                    start.x == end.x -> (start.y towards end.y).map { Coord(start.x, it) }
                    else -> error("diagonal paths not supported!")
                }
            }.associateWithTo(result) { true }
        val maxY = result.keys.maxOf { (_, y) -> y }
        return result to maxY
    }

    private fun MutableMap<Coord, Boolean>.simulateSand(maxY: Int): Int {
        val entry = Coord(500, 0)
        var sandCount = 0
        do {
            var atRest = false
            ++sandCount
            var currentPos = entry
            while (!atRest && currentPos.y < maxY) {
                val down = Coord(currentPos.x, currentPos.y + 1)
                val downLeft = Coord(currentPos.x - 1, currentPos.y + 1)
                val downRight = Coord(currentPos.x + 1, currentPos.y + 1)
                when {
                    !getValue(down) -> currentPos = down
                    !getValue(downLeft) -> currentPos = downLeft
                    !getValue(downRight) -> currentPos = downRight

                    else -> {
                        this[currentPos] = true
                        atRest = true
                    }
                }
            }
        } while (currentPos != entry && currentPos.y < maxY)
        return sandCount
    }

    override fun executePart1(input: String): Int {
        val (grid, maxY) = getRockGrid(input)
        return grid.withDefault { false }
            .simulateSand(maxY) - 1
    }

    override fun executePart2(input: String): Int {
        val (grid, maxY) = getRockGrid(input)
        return grid.withDefault { (_, y) -> y >= maxY + 2 }
            .simulateSand(maxY + 2)
    }
}