package com.example.there.domain.usecase.base

import io.reactivex.Flowable
import io.reactivex.Scheduler

abstract class BaseFlowableUseCase<Result>(
        subscribeOnScheduler: Scheduler,
        observeOnScheduler: Scheduler
) : BaseRxUseCase(subscribeOnScheduler, observeOnScheduler) {

    protected fun Flowable<Result>.applySchedulersIfRequested(
            applySchedulers: Boolean
    ): Flowable<Result> = run {
        if (applySchedulers)
            this.subscribeOn(subscribeOnScheduler).observeOn(observeOnScheduler)
        else this
    }
}

abstract class FlowableUseCase<Result>(
        subscribeOnScheduler: Scheduler,
        observeOnScheduler: Scheduler
) : BaseFlowableUseCase<Result>(subscribeOnScheduler, observeOnScheduler) {

    protected abstract val flowable: Flowable<Result>

    fun execute(
            applySchedulers: Boolean = true
    ): Flowable<Result> = flowable.applySchedulersIfRequested(applySchedulers)
}

abstract class FlowableUseCaseWithInput<Input, Result>(
        subscribeOnScheduler: Scheduler,
        observeOnScheduler: Scheduler
) : BaseFlowableUseCase<Result>(subscribeOnScheduler, observeOnScheduler) {

    protected abstract fun createFlowable(input: Input): Flowable<Result>

    fun execute(
            input: Input,
            applySchedulers: Boolean = true
    ): Flowable<Result> = createFlowable(input).applySchedulersIfRequested(applySchedulers)
}