package com.rtarita.days

import com.rtarita.structure.AoCDay
import com.rtarita.util.day
import com.rtarita.util.lcm
import kotlinx.datetime.LocalDate
import java.util.concurrent.LinkedTransferQueue

object Day11 : AoCDay {
    private val MONKEY_REGEX = Regex(
        "Monkey ([0-9]+):\n" +
                "\\s+Starting items: ([0-9]+(?:, [0-9]+)*)\n" +
                "\\s+Operation: new = old ([+*]) ([0-9]+|old)\n" +
                "\\s+Test: divisible by ([0-9]+)\n" +
                "\\s+If true: throw to monkey ([0-9]+)\n" +
                "\\s+If false: throw to monkey ([0-9]+)"
    )

    private open class Monkey(
        val number: Int,
        startingItems: List<Long>,
        val operation: (Long) -> Long,
        val testValue: Long,
        val trueTarget: Int,
        val falseTarget: Int
    ) {
        companion object {
            var lcm = Long.MAX_VALUE
        }

        val items = LinkedTransferQueue(startingItems)
        var inspectionCount = 0L
        fun inspectNextAndThrow(divide: Boolean): Pair<Long, Int> {
            val newWorryLevel = if (divide) operation(items.poll()) / 3 else operation(items.poll()) % lcm
            ++inspectionCount
            return newWorryLevel to if (newWorryLevel % testValue == 0L) trueTarget else falseTarget
        }
    }

    override val day: LocalDate = day(11)

    private fun plusValue(value: Long): (Long) -> Long = { it + value }
    private fun plusSelf(): (Long) -> Long = { it + it }
    private fun timesValue(value: Long): (Long) -> Long = { it * value }
    private fun timesSelf(): (Long) -> Long = { it * it }


    private fun parseMonkey(monkey: String): Monkey {
        val (
            number,
            startingItems,
            operationType,
            operationValue,
            testValue,
            trueTarget,
            falseTarget
        ) = MONKEY_REGEX.matchEntire(monkey)?.destructured ?: error("invalid monkey string: $monkey")
        val operation: (Long) -> Long = when (operationType) {
            "+" -> {
                if (operationValue == "old") {
                    plusSelf()
                } else {
                    plusValue(operationValue.toLong())
                }
            }

            "*" -> {
                if (operationValue == "old") {
                    timesSelf()
                } else {
                    timesValue(operationValue.toLong())
                }
            }

            else -> error("invalid operation: $operationType")
        }

        return Monkey(
            number.toInt(),
            startingItems.split(", ").map { it.toLong() },
            operation,
            testValue.toLong(),
            trueTarget.toInt(),
            falseTarget.toInt()
        )
    }

    private fun parseInput(input: String): Map<Int, Monkey> {
        return input.split("\n\n")
            .map { parseMonkey(it) }
            .associateBy { it.number }
    }

    private fun playKeepAway(monkeys: Map<Int, Monkey>, rounds: Int, divide: Boolean): Long {
        repeat(rounds) {
            for (monkey in monkeys.values) {
                while (monkey.items.isNotEmpty()) {
                    val (item, target) = monkey.inspectNextAndThrow(divide)
                    monkeys.getValue(target).items.add(item)
                }
            }
        }
        return monkeys.values
            .sortedByDescending { it.inspectionCount }
            .take(2)
            .fold(1) { acc, monkey -> acc * monkey.inspectionCount }
    }

    override fun executePart1(input: String): Long {
        val monkeys = parseInput(input)
        return playKeepAway(monkeys, 20, true)
    }

    override fun executePart2(input: String): Long {
        val monkeys = parseInput(input)
        Monkey.lcm = monkeys.values
            .map { it.testValue }
            .lcm()

        return playKeepAway(monkeys, 10_000, false)
    }
}