package com.rtarita.days

import com.rtarita.structure.AoCDay
import com.rtarita.util.day
import kotlinx.datetime.LocalDate

object Day10 : AoCDay {
    private sealed class Instruction(val cycleLength: Int)

    private object Noop : Instruction(1)

    private data class AddX(val value: Int) : Instruction(2)

    override val day: LocalDate = day(10)

    private fun getInstructions(input: String): Sequence<Instruction> {
        return input.lineSequence()
            .map {
                when {
                    it.startsWith("addx") -> AddX(it.substring(5).toInt())
                    else -> Noop
                }
            }
    }

    private fun doCycles(
        instructions: Iterator<Instruction>,
        cycleActionBefore: (Int, Int) -> Unit = { _, _ -> },
        cycleActionAfter: (Int, Int) -> Unit = { _, _ -> }
    ) {
        var xReg = 1
        var cycle = 0
        var currentInstr: Instruction = instructions.next()
        var cyclesLeft = currentInstr.cycleLength
        while (instructions.hasNext()) {
            cycleActionBefore(xReg, cycle)
            if (cyclesLeft == 0) {
                if (currentInstr is AddX) {
                    xReg += currentInstr.value
                }
                currentInstr = instructions.next()
                cyclesLeft = currentInstr.cycleLength
            }
            cycleActionAfter(xReg, cycle)
            --cyclesLeft
            ++cycle
        }
    }

    private fun doCyclesBefore(instructions: Iterator<Instruction>, cycleActionBefore: (Int, Int) -> Unit) {
        doCycles(instructions, cycleActionBefore = cycleActionBefore)
    }

    private fun doCyclesAfter(instructions: Iterator<Instruction>, cycleActionAfter: (Int, Int) -> Unit) {
        doCycles(instructions, cycleActionAfter = cycleActionAfter)
    }

    override fun executePart1(input: String): Any {
        var result = 0
        val instructions = getInstructions(input).iterator()
        doCyclesBefore(instructions) { xReg, cycle ->
            if ((cycle - 20) % 40 == 0) {
                result += xReg * cycle
            }
        }
        return result
    }

    override fun executePart2(input: String): Any {
        val crt = StringBuilder(40 * 6 + 6)
        var line = 0
        val instructions = getInstructions(input).iterator()
        doCyclesAfter(instructions) { xReg, cycle ->
            crt.append(
                if ((cycle - line * 40) in (xReg - 1)..(xReg + 1)) {
                    '#'
                } else {
                    '.'
                }
            )
            if ((cycle + 1) % 40 == 0) {
                crt.append('\n')
                ++line
            }
        }
        return crt.toString()
    }
}