package com.example.there.domain.util

val Long.timeString: String
    get() = if (this < 10) "0$this" else toString()
    