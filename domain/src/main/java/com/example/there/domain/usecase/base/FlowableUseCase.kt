package com.example.there.domain.usecase.base

import com.example.there.domain.UseCaseSchedulersProvider
import io.reactivex.Flowable

abstract class BaseFlowableUseCase<Result>(
        schedulersProvider: UseCaseSchedulersProvider
) : BaseRxUseCase(schedulersProvider) {

    protected fun Flowable<Result>.applySchedulersIfRequested(
            applySchedulers: Boolean
    ): Flowable<Result> = run {
        if (applySchedulers)
            this.subscribeOn(schedulersProvider.subscribeOnScheduler)
                    .observeOn(schedulersProvider.observeOnScheduler)
        else this
    }
}

abstract class FlowableUseCase<Result>(
        schedulersProvider: UseCaseSchedulersProvider
) : BaseFlowableUseCase<Result>(schedulersProvider) {

    protected abstract val flowable: Flowable<Result>

    fun execute(
            applySchedulers: Boolean = true
    ): Flowable<Result> = flowable.applySchedulersIfRequested(applySchedulers)
}

abstract class FlowableUseCaseWithInput<Input, Result>(
        schedulersProvider: UseCaseSchedulersProvider
) : BaseFlowableUseCase<Result>(schedulersProvider) {

    protected abstract fun createFlowable(input: Input): Flowable<Result>

    fun execute(
            input: Input,
            applySchedulers: Boolean = true
    ): Flowable<Result> = createFlowable(input).applySchedulersIfRequested(applySchedulers)
}