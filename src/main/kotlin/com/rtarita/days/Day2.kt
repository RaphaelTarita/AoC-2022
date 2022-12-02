package com.rtarita.days

import com.rtarita.structure.AoCDay
import com.rtarita.util.day
import kotlinx.datetime.LocalDate

object Day2 : AoCDay {
    private enum class Play(val opponentLetter: Char, val ownLetter: Char, val value: Int) {
        ROCK('X', 'A', 1),
        PAPER('Y', 'B', 2),
        SCISSORS('Z', 'C', 3);

        // use lazy because values() might not be available yet
        val losing by lazy { values()[(ordinal + 2) % values().size] }
        val drawing = this
        val winning by lazy { values()[(ordinal + 1) % values().size] }

        companion object {
            fun fromString(play: String): Play {
                val playChar = play.first()
                for (option in values()) {
                    if (option.opponentLetter == playChar || option.ownLetter == playChar) {
                        return option
                    }
                }
                error("not a valid play: $play")
            }
        }
    }

    private enum class Outcome(val letter: Char, val value: Int) {
        LOSS('X', 0),
        DRAW('Y', 3),
        WIN('Z', 6);

        companion object {
            fun fromString(str: String): Outcome {
                val char = str.first()
                for (option in values()) {
                    if (option.letter == char) {
                        return option
                    }
                }
                error("not a valid outcome: $str")
            }
        }
    }

    override val day: LocalDate = day(2)

    private fun evaluate(opponentPlay: Play, ownPlay: Play): Outcome {
        return when (ownPlay) {
            opponentPlay.losing -> Outcome.LOSS
            opponentPlay.drawing -> Outcome.DRAW
            opponentPlay.winning -> Outcome.WIN

            else -> error("not a valid round")
        }
    }

    private fun getOwnMove(opponentPlay: Play, outcome: Outcome): Play {
        return when (outcome) {
            Outcome.LOSS -> opponentPlay.losing
            Outcome.DRAW -> opponentPlay.drawing
            Outcome.WIN -> opponentPlay.winning
        }
    }

    private fun getInputSplit(from: String): Sequence<List<String>> {
        return from.lineSequence()
            .map { it.split(" ") }
    }

    override fun executePart1(input: String): Int {
        return getInputSplit(input).map { (a, b) -> Play.fromString(a) to Play.fromString(b) }
            .sumOf { (opponent, you) -> you.value + evaluate(opponent, you).value }
    }

    override fun executePart2(input: String): Int {
        return getInputSplit(input).map { (a, b) -> Play.fromString(a) to Outcome.fromString(b) }
            .sumOf { (opponent, outcome) -> getOwnMove(opponent, outcome).value + outcome.value }
    }
}