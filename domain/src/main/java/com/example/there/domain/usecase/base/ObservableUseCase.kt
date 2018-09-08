package com.example.there.domain.usecase.base

import com.example.there.domain.common.SymmetricObservableTransformer
import io.reactivex.Observable

abstract class ObservableUseCase<T>(private val transformer: SymmetricObservableTransformer<T>) {

    protected abstract fun createObservable(data: Map<String, Any?>? = null): Observable<T>

    fun execute(withData: Map<String, Any?>? = null): Observable<T> {
        return createObservable(withData).compose(transformer)
    }
}