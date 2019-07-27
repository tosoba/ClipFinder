package com.example.there.domain.usecase.base

import com.example.core.ext.RetryStrategy
import com.example.there.domain.UseCaseSchedulersProvider
import io.reactivex.Flowable


abstract class FlowableUseCase<Result>(
        schedulersProvider: UseCaseSchedulersProvider
) : BaseRxUseCase(schedulersProvider) {

    protected abstract val result: Flowable<Result>

    operator fun invoke(
            applySchedulers: Boolean = true, retry: RetryStrategy? = null
    ): Flowable<Result> = result.retryIfNeeded(retry)
            .applySchedulersIfRequested(applySchedulers)
}

abstract class FlowableUseCaseWithArgs<Args, Result>(
        schedulersProvider: UseCaseSchedulersProvider
) : BaseRxUseCase(schedulersProvider) {

    protected abstract fun run(args: Args): Flowable<Result>

    operator fun invoke(
            args: Args, applySchedulers: Boolean = true, retry: RetryStrategy? = null
    ): Flowable<Result> = run(args).retryIfNeeded(retry)
            .applySchedulersIfRequested(applySchedulers)
}