package com.example.there.domain.usecase.base

import com.example.core.ext.RetryStrategy
import com.example.there.domain.UseCaseSchedulersProvider
import io.reactivex.Single


abstract class SingleUseCase<Result>(
        schedulersProvider: UseCaseSchedulersProvider
) : BaseRxUseCase(schedulersProvider) {

    protected abstract val result: Single<Result>

    operator fun invoke(
            applySchedulers: Boolean = true, retry: RetryStrategy? = null
    ): Single<Result> = result.retryIfNeeded(retry)
            .applySchedulersIfRequested(applySchedulers)
}

abstract class SingleUseCaseWithArgs<Args, Res>(
        schedulersProvider: UseCaseSchedulersProvider
) : BaseRxUseCase(schedulersProvider) {

    protected abstract fun run(args: Args): Single<Res>

    operator fun invoke(
            args: Args, applySchedulers: Boolean = true, retry: RetryStrategy? = null
    ): Single<Res> = run(args).retryIfNeeded(retry)
            .applySchedulersIfRequested(applySchedulers)
}

