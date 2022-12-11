package com.rtarita.util

import kotlin.math.pow
import kotlin.math.ulp

fun min(a: Byte, b: Byte) = if (a < b) a else b
fun min(a: Short, b: Short) = if (a < b) a else b

fun max(a: Byte, b: Byte) = if (a > b) a else b
fun max(a: Short, b: Short) = if (a > b) a else b

fun min(a: Byte, b: Byte, c: Byte): Byte = min(min(a, b), c)
fun min(a: Short, b: Short, c: Short): Short = min(min(a, b), c)
fun min(a: Int, b: Int, c: Int): Int = kotlin.math.min(kotlin.math.min(a, b), c)
fun min(a: Long, b: Long, c: Long): Long = kotlin.math.min(kotlin.math.min(a, b), c)
fun min(a: Float, b: Float, c: Float): Float = kotlin.math.min(kotlin.math.min(a, b), c)
fun min(a: Double, b: Double, c: Double): Double = kotlin.math.min(kotlin.math.min(a, b), c)

fun max(a: Byte, b: Byte, c: Byte) = max(max(a, b), c)
fun max(a: Short, b: Short, c: Short) = max(max(a, b), c)
fun max(a: Int, b: Int, c: Int): Int = kotlin.math.max(kotlin.math.max(a, b), c)
fun max(a: Long, b: Long, c: Long): Long = kotlin.math.max(kotlin.math.max(a, b), c)
fun max(a: Float, b: Float, c: Float): Float = kotlin.math.max(kotlin.math.max(a, b), c)
fun max(a: Double, b: Double, c: Double): Double = kotlin.math.max(kotlin.math.max(a, b), c)

fun sqr(x: Byte): Int = x * x
fun sqr(x: Short): Int = x * x
fun sqr(x: Int): Int = x * x
fun sqr(x: Long): Long = x * x
fun sqr(x: Float): Float = x * x
fun sqr(x: Double): Double = x * x

fun Byte.coerce(lower: Byte, upper: Byte): Byte = min(max(this, lower), upper)
fun Short.coerce(lower: Short, upper: Short): Short = min(max(this, lower), upper)
fun Int.coerce(lower: Int, upper: Int): Int = kotlin.math.min(kotlin.math.max(this, lower), upper)
fun Long.coerce(lower: Long, upper: Long): Long = kotlin.math.min(kotlin.math.max(this, lower), upper)
fun Float.coerce(lower: Float, upper: Float): Float = kotlin.math.min(kotlin.math.max(this, lower), upper)
fun Double.coerce(lower: Double, upper: Double): Double = kotlin.math.min(kotlin.math.max(this, lower), upper)

fun Short.coerceToByte(): Byte = coerce(Byte.MIN_VALUE.toShort(), Byte.MAX_VALUE.toShort()).toByte()
fun Int.coerceToByte(): Byte = coerce(Byte.MIN_VALUE.toInt(), Byte.MAX_VALUE.toInt()).toByte()
fun Int.coerceToShort(): Short = coerce(Short.MIN_VALUE.toInt(), Short.MAX_VALUE.toInt()).toShort()
fun Long.coerceToByte(): Byte = coerce(Byte.MIN_VALUE.toLong(), Byte.MAX_VALUE.toLong()).toByte()
fun Long.coerceToShort(): Short = coerce(Short.MIN_VALUE.toLong(), Short.MAX_VALUE.toLong()).toShort()
fun Long.coerceToInt(): Int = coerce(Int.MIN_VALUE.toLong(), Int.MAX_VALUE.toLong()).toInt()
fun Double.coerceToFloat(): Float = coerce(Float.MIN_VALUE.toDouble(), Float.MAX_VALUE.toDouble()).toFloat()

fun sign(b: Byte): Int = if (b > 0) 1 else if (b < 0) -1 else 0
fun sign(s: Short): Int = if (s > 0) 1 else if (s < 0) -1 else 0
fun sign(i: Int): Int = if (i > 0) 1 else if (i < 0) -1 else 0
fun sign(l: Long): Int = if (l > 0) 1 else if (l < 0) -1 else 0
fun sign(f: Float): Int = if (f > 0) 1 else if (f < 0) -1 else 0
fun sign(d: Double): Int = if (d > 0) 1 else if (d < 0) -1 else 0

fun invertInsertionPoint(inverted: Int): Int = -(inverted + 1)

fun Int.pow(other: Double): Double = toDouble().pow(other)
fun Int.pow(other: Int): Double = toDouble().pow(other)
fun cbrt(a: Double): Double = Math.cbrt(a)
fun cbrt(a: Int): Double = Math.cbrt(a.toDouble())

infix fun Double.epsilonEq(other: Double): Boolean {
    val epsilon = kotlin.math.max(ulp, other.ulp)
    return this > (other - epsilon) && this < (other + epsilon)
}

fun Int.inModRange(a: Int, b: Int): Int {
    return this - (b - a) * ((this - a) / (b - a))
}

fun gcd(a: Long, b: Long): Long {
    var currentA = a
    var currentB = b
    while (currentB != 0L) {
        val temp = currentB
        currentB = currentA % currentB
        currentA = temp
    }
    return currentA
}

fun lcm(a: Long, b: Long): Long {
    return a * (b / gcd(a, b))
}