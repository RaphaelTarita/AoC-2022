package com.rtarita.days

import com.rtarita.structure.AoCDay
import com.rtarita.util.day
import kotlinx.datetime.LocalDate

object Day5 : AoCDay {
    private val MOVE_REGEX = "move\\s([0-9]+)\\sfrom\\s([0-9]+)\\sto\\s([0-9]+)".toRegex()

    private data class Move(val amount: Int, val from: Int, val to: Int)

    override val day: LocalDate = day(5)

    private fun parseInputs(input: String): Pair<MutableMap<Int, MutableList<Char>>, Sequence<Move>> {
        val (initStr, moveStr) = input.split("\n\n")
        val state = mutableMapOf<Int, MutableList<Char>>()
        initStr.lines()
            .dropLast(1) // not available on sequences
            .asSequence()
            .map { it.chunked(4) }
            .forEach {
                it.forEachIndexed { idx, str ->
                    if (str.isNotBlank()) {
                        state.merge(idx + 1, mutableListOf(str[1])) { prev, _ ->
                            prev += str[1]
                            prev
                        }
                    }
                }
            }

        state.forEach { (_, v) -> v.reverse() }

        val moves = moveStr.lineSequence()
            .map {
                val (_, amount, from, to) = MOVE_REGEX.matchEntire(it)?.groupValues ?: error("invalid move string")
                Move(amount.toInt(), from.toInt(), to.toInt())
            }

        return state to moves
    }

    private fun topCrates(state: Map<Int, List<Char>>): String {
        return state.entries
            .sortedBy { (k, _) -> k }
            .joinToString("") { (_, v) -> v.last().toString() }
    }

    override fun executePart1(input: String): String {
        val (state, moves) = parseInputs(input)

        for ((amount, from, to) in moves) {
            repeat(amount) {
                val crate = state.getValue(from).removeLast()
                state.getValue(to) += crate
            }
        }

        return topCrates(state)
    }

    override fun executePart2(input: String): String {
        val (state, moves) = parseInputs(input)

        for ((amount, from, to) in moves) {
            val crates = List(amount) { state.getValue(from).removeLast() }.asReversed()
            state.getValue(to) += crates
        }

        return topCrates(state)
    }
}