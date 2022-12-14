package com.rtarita.util

import java.util.EnumSet

typealias MapJoin<K, V, U> = (left: Map<K, V>, right: Map<K, U>) -> Map<K, Pair<V?, U?>>

fun ByteArray.chunked(size: Int, offset: Int = 0, until: Int = this.size): List<ByteArray> {
    val thisSize = until - offset
    val resultCapacity = thisSize / size
    val result = ArrayList<ByteArray>(resultCapacity)
    for (i in 0 until resultCapacity) {
        result.add(sliceArray((offset + i * size) until (offset + (i + 1) * size)))
    }
    val remaining = thisSize % size
    if (remaining > 0) {
        val last = ByteArray(size)
        result.add(copyInto(last, startIndex = until - remaining, endIndex = until))
    }

    return result
}

fun <T> List<T>.pad(toSize: Int, with: T): List<T> {
    if (toSize <= size) return this
    return List(toSize) { getOrNull(it) ?: with }
}

fun <T> List<T>.slice(indices: IntRange, padWith: T): List<T> {
    return subList(indices.first, indices.last + 1).pad(indices.last - indices.first, padWith)
}

fun <K, V, U> innerJoin(left: Map<K, V>, right: Map<K, U>): Map<K, Pair<V, U>> {
    val result = mutableMapOf<K, Pair<V, U>>()
    for ((key, firstValue) in left) {
        val rightValue = right[key]
        if (rightValue != null) {
            result[key] = firstValue to rightValue
        }
    }
    return result
}

fun <K, V, U> strictInnerJoin(left: Map<K, V>, right: Map<K, U>): Map<K, Pair<V, U>> {
    require(left.keys == right.keys) {
        "attempted to merge two maps with different key sets"
    }
    return innerJoin(left, right)
}

fun <K, V, U> leftOuterJoin(left: Map<K, V>, right: Map<K, U>): Map<K, Pair<V, U?>> {
    val result = mutableMapOf<K, Pair<V, U?>>()
    for ((key, leftValue) in left) {
        result[key] = leftValue to right[key]
    }
    return result
}

fun <K, V, U> rightOuterJoin(left: Map<K, V>, right: Map<K, U>): Map<K, Pair<V?, U>> {
    val result = mutableMapOf<K, Pair<V?, U>>()
    for ((key, rightValue) in right) {
        result[key] = left[key] to rightValue
    }
    return result
}

fun <K, V, U> fullOuterJoin(left: Map<K, V>, right: Map<K, U>): Map<K, Pair<V?, U?>> {
    val result = mutableMapOf<K, Pair<V?, U?>>()
    val rightMutable = right.toMutableMap()
    for ((key, value) in left) {
        result[key] = value to rightMutable.remove(key)
    }

    for ((key, value) in rightMutable) {
        result[key] = null to value
    }

    return result
}

fun <K, V, R> Map<K, V>.ifContainsKey(key: K, action: (Pair<K, V>) -> R): R? {
    return if (containsKey(key)) {
        action(key to this.getValue(key))
    } else {
        null
    }
}

fun <K, V, R> Map<K, V>.ifContainsValue(value: V, action: (Pair<K, V>) -> R): List<R> {
    return if (containsValue(value)) {
        val list = mutableListOf<R>()
        filterValues { it == value }.forEach {
            list.add(action(it.toPair()))
        }
        list
    } else {
        emptyList()
    }
}

fun <K, V> Iterable<Pair<K, V>>.toMap(onDuplicates: (Pair<K, V>) -> Unit): Map<K, V> {
    val res = LinkedHashMap<K, V>()
    for ((k, v) in this) {
        if (res.containsKey(k)) {
            onDuplicates(k to v)
        } else {
            res[k] = v
        }
    }
    return res
}

@Suppress("UNCHECKED_CAST")
internal fun <K, V> Map<K, V?>.filterNonNullValues(): Map<K, V> {
    return filterValues { it != null } as Map<K, V> // .mapValues { it.value!! }
}

internal fun <K, V> combineMaps(vararg maps: Map<K, V>): Map<K, V> {
    val destSize = maps.fold(0) { prev, cur -> prev + cur.size }
    val res = HashMap<K, V>(destSize)
    for (map in maps) {
        res.putAll(map)
    }
    return res
}

fun <T> Collection<T>.optimizeReadonlyCollection(): Collection<T> {
    return when (size) {
        0 -> emptyList()
        1 -> listOf(if (this is List) get(0) else iterator().next())
        else -> this
    }
}

fun <T, I : Iterable<T>> I.exactlyOrNull(n: Int): I? {
    return when (this) {
        is List<*> -> if (size == n) this else null
        else -> {
            val itr = iterator()
            repeat(n) {
                if (!itr.hasNext()) return@exactlyOrNull null
                itr.next()
            }
            if (itr.hasNext()) null else this
        }
    }
}

fun <T, I : Iterable<T>> I.exactly(n: Int): I {
    return exactlyOrNull(n) ?: throw IllegalArgumentException("Iterable does not have exactly $n elements")
}

fun <T> List<T>.withoutElementAtIndex(idx: Int): List<T> {
    val res = ArrayList(this)
    res.removeAt(idx)
    return res
}

infix fun <E : Enum<E>> EnumSet<E>.intersect(other: EnumSet<E>): EnumSet<E> {
    val res = EnumSet.copyOf(this)
    res.retainAll(other)
    return res
}

fun <T> Iterable<T>.splitAt(pos: Int): Pair<List<T>, List<T>> {
    val collectionSize = if (this is Collection) size else null
    if (pos <= 0) return emptyList<T>() to toList()
    if (collectionSize != null && pos >= collectionSize) return toList() to emptyList()

    val first = ArrayList<T>(pos)
    val second = ArrayList<T>(collectionSize?.minus(pos) ?: 10)
    for ((idx, t) in withIndex()) {
        (if (idx < pos) first else second) += t
    }
    return first to second
}

fun IntRange.containsAll(other: IntRange): Boolean {
    return first <= other.first && last >= other.last
}

infix fun IntRange.intersect(other: IntRange): IntRange {
    return if (this.first <= other.first) {
        other.first..this.last
    } else {
        this.first..other.last
    }
}

fun IntRange.isNotEmpty() = !isEmpty()

infix fun Int.towards(end: Int): IntProgression {
    return IntProgression.fromClosedRange(this, end, if (this < end) 1 else -1)
}

fun Iterable<Long>.gcd(): Long {
    return reduce { acc, elem -> gcd(acc, elem) }
}

fun Iterable<Long>.lcm(): Long {
    return reduce { acc, elem -> lcm(acc, elem) }
}