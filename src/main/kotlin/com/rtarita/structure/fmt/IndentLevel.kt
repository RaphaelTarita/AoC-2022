package com.rtarita.structure.fmt

import com.rtarita.util.repeat

class IndentLevel(private val level: Int) {
    init {
        require(level >= 0) {
            "indent level can't be less than 0"
        }
    }

    fun indent(config: IndentConfig, value: String): String {
        return config.indentType.character.repeat(config.indentCount * level) + value
    }

    fun <T> indent(config: IndentConfig, value: T, fmt: Formatter<T> = GenericFormatter): String {
        with(fmt) {
            return config.indentType.character.repeat(config.indentCount * level) + value.format()
        }
    }

    operator fun inc(): IndentLevel {
        check(level < Int.MAX_VALUE) {
            "can't increment: integer maximum reached"
        }
        return IndentLevel(level + 1)
    }

    operator fun dec(): IndentLevel {
        check(level > 0) {
            "can't decrement: level 0 reached"
        }
        return IndentLevel(level - 1)
    }
}