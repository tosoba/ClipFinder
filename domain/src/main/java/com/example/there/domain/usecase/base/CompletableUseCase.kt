package com.example.there.domain.usecase.base

import io.reactivex.Completable
import io.reactivex.CompletableTransformer

abstract class CompletableUseCase(private val transformer: CompletableTransformer) {

    protected abstract fun createCompletable(data: Map<String, Any?>? = null): Completable

    fun execute(withData: Map<String, Any?>? = null): Completable{
        return createCompletable(withData).compose(transformer)
    }
}