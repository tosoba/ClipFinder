package com.example.there.domain.usecase.base

import com.example.there.domain.UseCaseSchedulersProvider
import io.reactivex.Observable


abstract class ObservableUseCase<Result>(
        schedulersProvider: UseCaseSchedulersProvider
) : BaseRxUseCase(schedulersProvider) {

    protected abstract val result: Observable<Result>

    operator fun invoke(
            applySchedulers: Boolean = true
    ): Observable<Result> = result.applySchedulersIfRequested(applySchedulers)
}

abstract class ObservableUseCaseWithArgs<Args, Result>(
        schedulersProvider: UseCaseSchedulersProvider
) : BaseRxUseCase(schedulersProvider) {

    protected abstract fun run(args: Args): Observable<Result>

    operator fun invoke(
            args: Args, applySchedulers: Boolean = true
    ): Observable<Result> = run(args).applySchedulersIfRequested(applySchedulers)
}