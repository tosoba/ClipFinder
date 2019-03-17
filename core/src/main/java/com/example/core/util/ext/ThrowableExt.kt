package com.example.core.util.ext

fun Throwable.messageOrDefault(message: String = "Unknown error."): String {
    return this.message ?: message
}