package com.example.there.domain.usecase.base

import com.example.there.domain.common.SymmetricFlowableTransformer
import io.reactivex.Flowable

abstract class FlowableUseCase<T>(private val transformer: SymmetricFlowableTransformer<T>) {

    protected abstract fun createFlowable(data: Map<String, Any?>? = null): Flowable<T>

    fun execute(withData: Map<String, Any?>? = null): Flowable<T> {
        return createFlowable(withData).compose(transformer)
    }
}