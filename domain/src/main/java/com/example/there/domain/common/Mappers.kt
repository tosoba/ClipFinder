package com.example.there.domain.common

import io.reactivex.Observable

abstract class OneWayMapper<in E, T> {
    abstract fun mapFrom(from: E): T

    fun mapOptional(from: Optional<E>): Optional<T> {
        from.value?.let {
            return Optional.of(mapFrom(it))
        } ?: return Optional.empty()
    }

    fun observable(from: E): Observable<T> {
        return Observable.fromCallable { mapFrom(from) }
    }

    fun observable(from: List<E>): Observable<List<T>> {
        return Observable.fromCallable { from.map { mapFrom(it) } }
    }
}

abstract class TwoWayMapper<E, T> : OneWayMapper<E, T>() {
    abstract fun mapBack(from: T): E

    fun mapOptionalBack(from: Optional<T>): Optional<E> {
        from.value?.let {
            return Optional.of(mapBack(it))
        } ?: return Optional.empty()
    }

    fun observableBack(from: T): Observable<E> {
        return Observable.fromCallable { mapBack(from) }
    }

    fun observableBack(from: List<T>): Observable<List<E>> {
        return Observable.fromCallable { from.map { mapBack(it) } }
    }
}