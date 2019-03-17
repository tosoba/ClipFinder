package com.example.there.domain.usecase.base

import com.example.there.domain.UseCaseSchedulersProvider
import io.reactivex.Single

abstract class BaseSingleUseCase<Result>(
        schedulersProvider: UseCaseSchedulersProvider
) : BaseRxUseCase(schedulersProvider) {

    protected fun Single<Result>.applySchedulersIfRequested(
            applySchedulers: Boolean
    ): Single<Result> = run {
        if (applySchedulers)
            this.subscribeOn(schedulersProvider.subscribeOnScheduler)
                    .observeOn(schedulersProvider.observeOnScheduler)
        else this
    }
}

abstract class SingleUseCase<Result>(
        schedulersProvider: UseCaseSchedulersProvider
) : BaseSingleUseCase<Result>(schedulersProvider) {

    protected abstract val single: Single<Result>

    fun execute(
            applySchedulers: Boolean = true
    ): Single<Result> = single.applySchedulersIfRequested(applySchedulers)
}

abstract class SingleUseCaseWithInput<Input, Result>(
        schedulersProvider: UseCaseSchedulersProvider
) : BaseSingleUseCase<Result>(schedulersProvider) {

    protected abstract fun createSingle(input: Input): Single<Result>

    fun execute(
            input: Input,
            applySchedulers: Boolean = true
    ): Single<Result> = createSingle(input).applySchedulersIfRequested(applySchedulers)
}