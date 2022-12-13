package com.rtarita.util

fun String.replaceMultiple(map: Map<String, String>, ignoreCase: Boolean = false): String {
    var ret = this
    for ((old, new) in map) {
        ret = ret.replace(old, new, ignoreCase)
    }
    return ret
}

fun String.replaceMultiple(vararg entries: Pair<String, String>, ignoreCase: Boolean = false): String {
    return replaceMultiple(mapOf(*entries), ignoreCase)
}

fun String.escape(vararg charsToEscape: Char, ignoreCase: Boolean = false): String {
    return replaceMultiple(charsToEscape.associate { it.toString() to "\\" + it }, ignoreCase)
}

fun String.unescape(vararg charsToUnescape: Char, ignoreCase: Boolean = false): String {
    return replaceMultiple(charsToUnescape.associate { "\\" + it to it.toString() }, ignoreCase)
}

fun String.truncate(maxLen: Int, replacement: String = "..."): String {
    val actualMaxLen = maxLen - replacement.length
    require(actualMaxLen >= 0) { "The truncation replacement has to be smaller than the length bound" }
    return if (length <= actualMaxLen) this else substring(0..actualMaxLen) + replacement
}

fun String.indexOf(regex: Regex, startIndex: Int = 0, notFound: Int = 0): Int {
    return regex.find(this.substring(startIndex))?.range?.start ?: notFound
}

fun String.indexOfFirst(offset: Int, until: Int = lastIndex, predicate: (Char) -> Boolean): Int {
    for (index in offset..until) {
        if (predicate(this[index])) {
            return index
        }
    }
    return -1
}

fun String.indexOfLast(offset: Int, until: Int = lastIndex, predicate: (Char) -> Boolean): Int {
    for (index in (offset..until).reversed()) {
        if (predicate(this[index])) {
            return index
        }
    }
    return -1
}

fun String.indexOfExcluding(
    char: Char,
    exclusionPrefix: String,
    startIndex: Int = 0,
    ignoreCase: Boolean = false,
    ignorePrefixCase: Boolean = false
): Int {
    var exclusionProgress = 0
    var skipNext = false
    for (index in startIndex.coerceAtLeast(0)..lastIndex) {
        if (skipNext) continue

        val current = get(index)

        if (exclusionPrefix[exclusionProgress].equals(current, ignorePrefixCase)) {
            if (++exclusionProgress >= exclusionPrefix.length) {
                skipNext = true
                exclusionProgress = 0
            }
        } else {
            exclusionProgress = 0
        }

        if (current.equals(char, ignoreCase)) return index
    }

    return -1
}

private fun matching(symbol: Char): Char = when (symbol) {
    '(' -> ')'
    '[' -> ']'
    '{' -> '}'
    '<' -> '>'
    else -> throw IllegalArgumentException("Character '$symbol' has no matching counterpart")
}

fun String.indexOfMatching(symbol: Char, offset: Int = 0, matching: Char = matching(symbol)): Pair<Int, Int> {
    var level = 0
    var first = -1
    for (i in offset..this.lastIndex) {
        if (get(i) == symbol) {
            if (first == -1) first = i
            ++level
        }
        if (get(i) == matching) --level
        if (first != -1 && level == 0) return first to i
    }
    return first to -1
}

fun String.splitTopLevel(
    vararg matchingChars: Char = charArrayOf('(', '[', '{', '<'),
    startIndex: Int = 0,
    endIndex: Int = lastIndex,
    trim: Boolean = false
): List<String> {
    val offsetInternal = if (trim) {
        indexOfFirst(startIndex.coerceAtLeast(0), endIndex.coerceAtMost(lastIndex)) { !it.isWhitespace() }
    } else {
        startIndex.coerceAtLeast(0)
    }
    val endInternal = if (trim) {
        indexOfLast(startIndex.coerceAtLeast(0), endIndex.coerceAtMost(lastIndex)) { !it.isWhitespace() }
    } else {
        endIndex.coerceAtMost(lastIndex)
    }
    val result = mutableListOf<String>()
    val accumulator = StringBuilder()

    var i = offsetInternal
    while (i <= endInternal) {
        val cur = get(i)

        val prev = i
        when (cur) {
            in matchingChars -> i = indexOfMatching(cur, i).second
            '\"', '\'' -> i = indexOfExcluding(cur, "\\", i + 1)
            ',' -> {
                result.add(accumulator.toString())
                accumulator.clear()
                i = indexOfFirst(i + 1, endInternal) { !it.isWhitespace() }
                continue
            }
        }

        accumulator.append(if (prev == i) cur else substring(prev..i))
        i++
    }
    result.add(accumulator.toString())
    return result
}

fun String.isNumeric(): Boolean {
    return toIntOrNull() != null
}

fun String.capitalizeFirst(): String {
    return replaceFirstChar {
        if (it.isLowerCase()) it.titlecase() else it.toString()
    }
}

fun Char.repeat(n: Int): String {
    return when (n) {
        0 -> ""
        1 -> this.toString()
        else -> String(CharArray(n) { this })
    }
}