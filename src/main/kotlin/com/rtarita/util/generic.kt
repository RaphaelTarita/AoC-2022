package com.rtarita.util

typealias Twin<T> = Pair<T, T>

inline fun <reified E> ignoreError(block: () -> Unit) {
    try {
        block()
    } catch (exc: Throwable) {
        if (!E::class.isInstance(exc)) throw exc
    }
}

inline fun <reified E : Throwable, R> nullIfError(block: () -> R): R? {
    return try {
        block()
    } catch (exc: Throwable) {
        if (E::class.isInstance(exc)) null else throw exc
    }
}

fun hashCode(vararg vals: Any?, prime: Int = 31): Int {
    var res = 0
    for (v in vals) {
        res += v.hashCode()
        res *= prime
    }
    return res
}

inline fun <reified E : Enum<E>> Iterable<E>.negate(): Set<E> {
    return enumValues<E>().toSet() - if (this is Set) this else this.toSet()
}