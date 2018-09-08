package com.example.there.domain.usecase.base

import com.example.there.domain.common.SymmetricSingleTransformer
import io.reactivex.Single

abstract class SingleUseCase<T>(private val transformer: SymmetricSingleTransformer<T>) {

    protected abstract fun createSingle(data: Map<String, Any?>? = null): Single<T>

    fun execute(withData: Map<String, Any?>? = null): Single<T> {
        return createSingle(withData).compose(transformer)
    }
}