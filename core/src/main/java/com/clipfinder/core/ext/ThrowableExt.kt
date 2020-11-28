package com.clipfinder.core.ext

fun Throwable.messageOrDefault(default: String = "Unknown error."): String = message ?: default
