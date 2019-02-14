package com.example.there.domain.usecase.base

import io.reactivex.Observable
import io.reactivex.Scheduler

abstract class BaseObservableUseCase<Result>(
        subscribeOnScheduler: Scheduler,
        observeOnScheduler: Scheduler
) : BaseRxUseCase(subscribeOnScheduler, observeOnScheduler) {

    protected fun Observable<Result>.applySchedulersIfRequested(
            applySchedulers: Boolean
    ): Observable<Result> = run {
        if (applySchedulers)
            this.subscribeOn(subscribeOnScheduler).observeOn(observeOnScheduler)
        else this
    }
}

abstract class ObservableUseCase<Result>(
        subscribeOnScheduler: Scheduler,
        observeOnScheduler: Scheduler
) : BaseObservableUseCase<Result>(subscribeOnScheduler, observeOnScheduler) {

    protected abstract val observable: Observable<Result>

    fun execute(
            applySchedulers: Boolean = true
    ): Observable<Result> = observable.applySchedulersIfRequested(applySchedulers)
}

abstract class ObservableUseCaseWithInput<Input, Result>(
        subscribeOnScheduler: Scheduler,
        observeOnScheduler: Scheduler
) : BaseObservableUseCase<Result>(subscribeOnScheduler, observeOnScheduler) {

    protected abstract fun createObservable(input: Input): Observable<Result>

    fun execute(
            input: Input,
            applySchedulers: Boolean = true
    ): Observable<Result> = createObservable(input).applySchedulersIfRequested(applySchedulers)
}