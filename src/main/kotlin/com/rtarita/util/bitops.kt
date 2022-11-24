package com.rtarita.util

val HEX_CHARS = charArrayOf(
    '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'
)
val HEX_INV = arrayOf(
    "0000", "0001", "0010", "0011", "0100", "0101", "0110", "0111", "1000", "1001", "1010", "1011", "1100", "1101", "1110", "1111"
)

fun ByteArray.toHexString(offset: Int = 0): String {
    val hexChars = CharArray(size * 2)
    for (i in indices) {
        val v = this[i + offset].toInt() and 0xFF
        hexChars[i * 2] = HEX_CHARS[v ushr 4]
        hexChars[i * 2 + 1] = HEX_CHARS[v and 0x0F]
    }
    return hexChars.joinToString("")
}

fun UShort.msb(): Byte = (this.toInt() ushr 8).toByte()
fun UShort.lsb(): Byte = toByte()

fun Short.toByteArray(): ByteArray {
    return byteArrayOf(
        (toInt() ushr 8).toByte(),
        toByte()
    )
}

fun Int.toByteArray(): ByteArray {
    return byteArrayOf(
        (this ushr 24).toByte(),
        (this ushr 16).toByte(),
        (this ushr 8).toByte(),
        toByte()
    )
}

fun Long.toByteArray(): ByteArray {
    return byteArrayOf(
        (this ushr 56).toByte(),
        (this ushr 48).toByte(),
        (this ushr 40).toByte(),
        (this ushr 32).toByte(),
        (this ushr 24).toByte(),
        (this ushr 16).toByte(),
        (this ushr 8).toByte(),
        toByte()
    )
}

fun Float.toByteArray(): ByteArray = toRawBits().toByteArray()
fun Double.toByteArray(): ByteArray = toRawBits().toByteArray()