package com.example.there.domain.usecase.base

import com.example.core.ext.RetryStrategy
import com.example.there.domain.UseCaseSchedulersProvider
import io.reactivex.Observable

abstract class ObservableUseCase<Result>(
    schedulersProvider: UseCaseSchedulersProvider
) : BaseRxUseCase(schedulersProvider) {

    protected abstract val result: Observable<Result>

    operator fun invoke(
        applySchedulers: Boolean = true,
        retry: RetryStrategy? = null
    ): Observable<Result> = result.retryIfNeeded(retry)
        .applySchedulersIfRequested(applySchedulers)
}

abstract class ObservableUseCaseWithArgs<Args, Result>(
    schedulersProvider: UseCaseSchedulersProvider
) : BaseRxUseCase(schedulersProvider) {

    protected abstract fun run(args: Args): Observable<Result>

    operator fun invoke(
        args: Args,
        applySchedulers: Boolean = true,
        retry: RetryStrategy? = null
    ): Observable<Result> = run(args).retryIfNeeded(retry)
        .applySchedulersIfRequested(applySchedulers)
}
