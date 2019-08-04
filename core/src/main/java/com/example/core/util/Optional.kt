package com.example.core.util

sealed class Optional<T> {
    class Some<T>(val value: T) : Optional<T>()
    class None<T> : Optional<T>()

    companion object {
        fun <T> of(value: T?): Optional<T> = if (value == null) None() else Some(value)
    }
}
