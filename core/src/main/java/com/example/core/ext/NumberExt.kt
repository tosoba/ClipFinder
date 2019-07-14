package com.example.core.ext

import java.math.BigInteger
import java.util.*

private val suffixes = TreeMap<Long, String>().apply {
    put(1_000L, "K")
    put(1_000_000L, "M")
    put(1_000_000_000L, "G")
}

val BigInteger.formattedString: String
    get() {
        val value = toLong()
        if (value < 0) return "0"
        if (value < 1000) return value.toString()
        val (divideBy, suffix) = suffixes.floorEntry(value)
        val truncated = value / (divideBy / 10)
        val hasDecimal = truncated < 100 && truncated / 10.0 != (truncated / 10).toDouble()
        return if (hasDecimal) "${truncated / 10.0}$suffix" else "${truncated / 10}$suffix"
    }

fun Long.getPublishedAgoString(prefix: String): String = if (this == 1L) {
    "$this $prefix ago"
} else {
    "$this ${prefix}s ago"
}
