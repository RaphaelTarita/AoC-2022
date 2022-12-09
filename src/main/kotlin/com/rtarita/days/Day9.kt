package com.rtarita.days

import com.rtarita.structure.AoCDay
import com.rtarita.util.day
import com.rtarita.util.sign
import kotlinx.datetime.LocalDate
import kotlin.math.abs
import kotlin.math.max

object Day9 : AoCDay {
    private data class Coord(val x: Int, val y: Int)

    override val day: LocalDate = day(9)

    private fun getMovements(input: String): Sequence<Pair<Char, Int>> {
        return input.lineSequence()
            .map { it.split(' ') }
            .map { (c, i) ->
                c.first() to i.toInt()
            }
    }

    private fun calculateHeadMovement(hpos: Coord, dir: Char): Coord {
        return when (dir) {
            'L' -> Coord(hpos.x - 1, hpos.y)
            'U' -> Coord(hpos.x, hpos.y - 1)
            'R' -> Coord(hpos.x + 1, hpos.y)
            'D' -> Coord(hpos.x, hpos.y + 1)
            else -> error("unknown direction: $dir")
        }
    }

    private fun calculateTailMovement(tpos: Coord, hpos: Coord): Coord {
        val xdelta = hpos.x - tpos.x
        val ydelta = hpos.y - tpos.y
        return when {
            abs(xdelta) > 1 && abs(ydelta) > 1 -> Coord(
                tpos.x + sign(xdelta) * max(0, abs(xdelta) - 1),
                tpos.y + sign(ydelta) * max(0, abs(ydelta) - 1)
            )

            abs(xdelta) > 1 -> Coord(
                tpos.x + sign(xdelta) * max(0, abs(xdelta) - 1),
                tpos.y + ydelta
            )

            abs(ydelta) > 1 -> Coord(
                tpos.x + xdelta,
                tpos.y + sign(ydelta) * max(0, abs(ydelta) - 1)
            )

            abs(xdelta) <= 1 && abs(ydelta) <= 1 -> tpos
            else -> error("invalid game state, tpos($tpos), hpos($hpos)")
        }
    }

    override fun executePart1(input: String): Int {
        val visited = mutableSetOf<Coord>()

        getMovements(input)
            .fold(Pair(Coord(0, 0), Coord(0, 0))) { (hpos, tpos), (dir, dist) ->
                visited += tpos
                var currentH = hpos
                var currentT = tpos
                repeat(dist) {
                    currentH = calculateHeadMovement(currentH, dir)
                    currentT = calculateTailMovement(currentT, currentH)
                    visited += currentT
                }
                currentH to currentT
            }

        return visited.size
    }

    override fun executePart2(input: String): Any {
        val visited = mutableSetOf<Coord>()

        getMovements(input)
            .fold(List(10) { Coord(0, 0) }) { knotPositions, (dir, dist) ->
                visited += knotPositions.last()
                val newKnots = knotPositions.toMutableList()
                repeat(dist) {
                    newKnots[0] = calculateHeadMovement(newKnots[0], dir)
                    for (i in 1..knotPositions.lastIndex) {
                        newKnots[i] = calculateTailMovement(newKnots[i], newKnots[i - 1])
                    }
                    visited += newKnots.last()
                }
                newKnots
            }

        return visited.size
    }
}