package com.example.core.ext

inline fun <reified R> Any.castAs(): R? = this as? R
