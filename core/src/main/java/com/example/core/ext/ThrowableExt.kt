package com.example.core.ext

fun Throwable.messageOrDefault(message: String = "Unknown error."): String {
    return this.message ?: message
}