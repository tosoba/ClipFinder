package com.example.there.domain.usecase.base

import com.example.there.domain.UseCaseSchedulersProvider
import io.reactivex.Completable

abstract class BaseCompletableUseCase(
        schedulersProvider: UseCaseSchedulersProvider
) : BaseRxUseCase(schedulersProvider) {

    protected fun Completable.applySchedulersIfRequested(
            applySchedulers: Boolean
    ): Completable = run {
        if (applySchedulers)
            this.subscribeOn(schedulersProvider.subscribeOnScheduler)
                    .observeOn(schedulersProvider.observeOnScheduler)
        else this
    }
}

abstract class CompletableUseCase(
        schedulersProvider: UseCaseSchedulersProvider
) : BaseCompletableUseCase(schedulersProvider) {

    protected abstract val completable: Completable

    fun execute(
            applySchedulers: Boolean = true
    ): Completable = completable.applySchedulersIfRequested(applySchedulers)
}

abstract class CompletableUseCaseWithInput<Input>(
        schedulersProvider: UseCaseSchedulersProvider
) : BaseCompletableUseCase(schedulersProvider) {

    protected abstract fun createCompletable(input: Input): Completable

    fun execute(
            input: Input,
            applySchedulers: Boolean = true
    ): Completable = createCompletable(input).applySchedulersIfRequested(applySchedulers)
}