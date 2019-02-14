package com.example.there.domain.usecase.base

import io.reactivex.Scheduler
import io.reactivex.Single

abstract class BaseSingleUseCase<Result>(
        subscribeOnScheduler: Scheduler,
        observeOnScheduler: Scheduler
) : BaseRxUseCase(subscribeOnScheduler, observeOnScheduler) {

    protected fun Single<Result>.applySchedulersIfRequested(
            applySchedulers: Boolean
    ): Single<Result> = run {
        if (applySchedulers)
            this.subscribeOn(subscribeOnScheduler).observeOn(observeOnScheduler)
        else this
    }
}

abstract class SingleUseCase<Result>(
        subscribeOnScheduler: Scheduler,
        observeOnScheduler: Scheduler
) : BaseSingleUseCase<Result>(subscribeOnScheduler, observeOnScheduler) {

    protected abstract val single: Single<Result>

    fun execute(
            applySchedulers: Boolean = true
    ): Single<Result> = single.applySchedulersIfRequested(applySchedulers)
}

abstract class SingleUseCaseWithInput<Input, Result>(
        subscribeOnScheduler: Scheduler,
        observeOnScheduler: Scheduler
) : BaseSingleUseCase<Result>(subscribeOnScheduler, observeOnScheduler) {

    protected abstract fun createSingle(input: Input): Single<Result>

    fun execute(
            input: Input,
            applySchedulers: Boolean = true
    ): Single<Result> = createSingle(input).applySchedulersIfRequested(applySchedulers)
}