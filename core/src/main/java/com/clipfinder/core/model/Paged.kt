package com.clipfinder.core.model

data class Paged<out T>(val contents: T, val offset: Int, val total: Int)
