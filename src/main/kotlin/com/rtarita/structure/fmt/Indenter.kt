package com.rtarita.structure.fmt

class Indenter(private val config: IndentConfig) {
    private var current = IndentLevel(0)
    private val accumulator = StringBuilder()

    fun increaseLevel(): Indenter {
        ++current
        return this
    }

    fun decreaseLevel(): Indenter {
        --current
        return this
    }

    fun setLevel(n: Int): Indenter {
        current = IndentLevel(n)
        return this
    }

    fun resetLevel(): Indenter {
        current = IndentLevel(0)
        return this
    }

    fun indent(value: String): String {
        return current.indent(config, value)
    }

    fun <T> indent(value: T, fmt: Formatter<T> = GenericFormatter): String {
        return current.indent(config, value, fmt)
    }

    fun append(value: String): Indenter {
        accumulator.append(indent(value))
            .append('\n')

        return this
    }

    fun <T> append(value: T, fmt: Formatter<T> = GenericFormatter): Indenter {
        accumulator.append(indent(value, fmt))
            .append('\n')

        return this
    }

    fun getText(): String = accumulator.toString()
}