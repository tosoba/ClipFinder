package com.example.there.domain.usecase.base

import com.example.core.ext.RetryStrategy
import com.example.there.domain.UseCaseSchedulersProvider
import io.reactivex.Completable

abstract class CompletableUseCase(
    schedulersProvider: UseCaseSchedulersProvider
) : BaseRxUseCase(schedulersProvider) {

    protected abstract val result: Completable

    operator fun invoke(
        applySchedulers: Boolean = true,
        retry: RetryStrategy? = null
    ): Completable = result.retryIfNeeded(retry)
        .applySchedulersIfRequested(applySchedulers)
}

abstract class CompletableUseCaseWithArgs<Args>(
    schedulersProvider: UseCaseSchedulersProvider
) : BaseRxUseCase(schedulersProvider) {

    protected abstract fun run(args: Args): Completable

    operator fun invoke(
        args: Args,
        applySchedulers: Boolean = true,
        retry: RetryStrategy? = null
    ): Completable = run(args).retryIfNeeded(retry)
        .applySchedulersIfRequested(applySchedulers)
}
