package com.example.there.domain.usecase.base

import com.example.there.domain.UseCaseSchedulersProvider
import io.reactivex.Observable

abstract class BaseObservableUseCase<Result>(
        schedulersProvider: UseCaseSchedulersProvider
) : BaseRxUseCase(schedulersProvider) {

    protected fun Observable<Result>.applySchedulersIfRequested(
            applySchedulers: Boolean
    ): Observable<Result> = run {
        if (applySchedulers) this.subscribeOn(schedulersProvider.subscribeOnScheduler)
                .observeOn(schedulersProvider.observeOnScheduler)
        else this
    }
}

abstract class ObservableUseCase<Result>(
        schedulersProvider: UseCaseSchedulersProvider
) : BaseObservableUseCase<Result>(schedulersProvider) {

    protected abstract val observable: Observable<Result>

    operator fun invoke(
            applySchedulers: Boolean = true
    ): Observable<Result> = observable.applySchedulersIfRequested(applySchedulers)
}

abstract class ObservableUseCaseWithArgs<Args, Result>(
        schedulersProvider: UseCaseSchedulersProvider
) : BaseObservableUseCase<Result>(schedulersProvider) {

    protected abstract fun createObservable(args: Args): Observable<Result>

    operator fun invoke(
            args: Args, applySchedulers: Boolean = true
    ): Observable<Result> = createObservable(args).applySchedulersIfRequested(applySchedulers)
}