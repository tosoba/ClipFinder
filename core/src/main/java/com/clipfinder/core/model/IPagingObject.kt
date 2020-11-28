package com.clipfinder.core.model

interface IPagingObject<out T : Any> {
    val items: List<T>
    val offset: Int
    val total: Int
}
