package com.rtarita.days

import com.rtarita.structure.AoCDay
import com.rtarita.util.day
import kotlinx.datetime.LocalDate

object Day8 : AoCDay {
    override val day: LocalDate = day(8)
    private fun getGrid(input: String): List<List<Int>> {
        return input.lineSequence()
            .map { line ->
                line.asSequence().map {
                    it.digitToInt()
                }.toList()
            }.toList()
    }

    private inline fun <R> List<List<Int>>.calculate(x: Int, y: Int, calculation: (gridval: Int, xList: List<Int>, yList: List<Int>) -> R): R {
        return calculation(this[y][x], this[y], List(size) { this[it][x] })
    }

    override fun executePart1(input: String): Int {
        val grid = getGrid(input)
        return grid.indices.sumOf { y ->
            grid[y].indices.count { x ->
                grid.calculate(x, y) { height, xList, yList ->
                    (xList.subList(0, x).maxOrNull() ?: -1) < height
                            || (xList.subList(x + 1, xList.size).maxOrNull() ?: -1) < height
                            || (yList.subList(0, y).maxOrNull() ?: -1) < height
                            || (yList.subList(y + 1, yList.size).maxOrNull() ?: -1) < height
                }
            }
        }
    }

    override fun executePart2(input: String): Int {
        val grid = getGrid(input)
        return grid.indices.maxOf { y ->
            grid[y].indices.maxOf { x ->
                grid.calculate(x, y) { height, xList, yList ->
                    if (x == 0 || x == xList.lastIndex || y == 0 || y == yList.lastIndex) {
                        0
                    } else {
                        (xList.subList(1, x).takeLastWhile { it < height }.count() + 1) *
                                (xList.subList(x + 1, xList.lastIndex).takeWhile { it < height }.count() + 1) *
                                (yList.subList(1, y).takeLastWhile { it < height }.count() + 1) *
                                (yList.subList(y + 1, yList.lastIndex).takeWhile { it < height }.count() + 1)
                    }
                }
            }
        }
    }
}