package com.example.core.ext

fun Throwable.messageOrDefault(default: String = "Unknown error."): String = message ?: default
