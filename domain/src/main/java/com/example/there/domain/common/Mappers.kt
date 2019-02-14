package com.example.there.domain.common

abstract class OneWayMapper<in E, T> {
    abstract fun mapFrom(from: E): T
}

abstract class TwoWayMapper<E, T> : OneWayMapper<E, T>() {
    abstract fun mapBack(from: T): E
}