package com.rtarita.util

sealed class NullableCompare<T> {
    data class RESULT<T>(val res: Int) : NullableCompare<T>()
    data class CONTINUE<T>(val o1: T, val o2: T) : NullableCompare<T>()
}

fun <T> compareNullable(n1: T?, n2: T?): NullableCompare<T> {
    return if (n1 == null) {
        if (n2 == null) NullableCompare.RESULT(0)
        else NullableCompare.RESULT(-1)
    } else {
        if (n2 == null) NullableCompare.RESULT(1)
        else NullableCompare.CONTINUE(n1, n2)
    }
}

fun <T, U> comparatorForNested(innerComparator: Comparator<U>, selector: (T) -> U): Comparator<T> {
    return Comparator { o1, o2 ->
        when (val ncomp = compareNullable(o1, o2)) {
            is NullableCompare.RESULT -> ncomp.res
            is NullableCompare.CONTINUE -> innerComparator.compare(selector(ncomp.o1), selector(ncomp.o2))
        }
    }
}

fun <T, U : Comparable<U>> minOfBy(first: T, vararg others: T, selector: (T) -> U): T {
    val min = others.minByOrNull(selector) ?: return first
    return if (selector(min) < selector(first)) min else first
}

fun <T, U : Comparable<U>> maxOfBy(first: T, vararg others: T, selector: (T) -> U): T {
    val max = others.maxByOrNull(selector) ?: return first
    return if (selector(max) > selector(first)) max else first
}

// inspired by: https://github.com/korlibs/kds
// file:        kds/src/commonMain/kotlin/com/soywiz/kds/comparator/ComparatorExt.kt
// revision:    cc2397e7a575412dfa6e3850317c59b168356b76
// date:        15.12.2021
object ComparatorByComparable : Comparator<Comparable<Any>> {
    override fun compare(o1: Comparable<Any>, o2: Comparable<Any>): Int = o1.compareTo(o2)
}