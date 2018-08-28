package com.example.there.domain.usecases

import com.example.there.domain.common.Transformer
import io.reactivex.Observable

abstract class UseCase<T>(private val transformer: Transformer<T>) {

    protected abstract fun createObservable(data: Map<String, Any?>? = null): Observable<T>

    fun execute(withData: Map<String, Any?>? = null): Observable<T> {
        return createObservable(withData).compose(transformer)
    }
}