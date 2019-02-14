package com.example.there.domain.usecase.base

import io.reactivex.Completable
import io.reactivex.Scheduler

abstract class BaseCompletableUseCase(
        subscribeOnScheduler: Scheduler,
        observeOnScheduler: Scheduler
) : BaseRxUseCase(subscribeOnScheduler, observeOnScheduler) {

    protected fun Completable.applySchedulersIfRequested(
            applySchedulers: Boolean
    ): Completable = run {
        if (applySchedulers)
            this.subscribeOn(subscribeOnScheduler).observeOn(observeOnScheduler)
        else this
    }
}

abstract class CompletableUseCase(
        subscribeOnScheduler: Scheduler,
        observeOnScheduler: Scheduler
) : BaseCompletableUseCase(subscribeOnScheduler, observeOnScheduler) {

    protected abstract val completable: Completable

    fun execute(
            applySchedulers: Boolean = true
    ): Completable = completable.applySchedulersIfRequested(applySchedulers)
}

abstract class CompletableUseCaseWithInput<Input>(
        subscribeOnScheduler: Scheduler,
        observeOnScheduler: Scheduler
) : BaseCompletableUseCase(subscribeOnScheduler, observeOnScheduler) {

    protected abstract fun createCompletable(input: Input): Completable

    fun execute(
            input: Input,
            applySchedulers: Boolean = true
    ): Completable = createCompletable(input).applySchedulersIfRequested(applySchedulers)
}