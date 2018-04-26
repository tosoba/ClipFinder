package com.example.there.domain.util

val Long.timeString: String
    get() {
        return if (this < 10) "0$this"
        else toString()
    }