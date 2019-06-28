package com.example.there.domain.usecase.base

import com.example.there.domain.UseCaseSchedulersProvider
import io.reactivex.Completable

abstract class BaseCompletableUseCase(
        schedulersProvider: UseCaseSchedulersProvider
) : BaseRxUseCase(schedulersProvider) {

    protected fun Completable.applySchedulersIfRequested(
            applySchedulers: Boolean
    ): Completable = run {
        if (applySchedulers) this.subscribeOn(schedulersProvider.subscribeOnScheduler)
                .observeOn(schedulersProvider.observeOnScheduler)
        else this
    }
}

abstract class CompletableUseCase(
        schedulersProvider: UseCaseSchedulersProvider
) : BaseCompletableUseCase(schedulersProvider) {

    protected abstract val completable: Completable

    operator fun invoke(
            applySchedulers: Boolean = true
    ): Completable = completable.applySchedulersIfRequested(applySchedulers)
}

abstract class CompletableUseCaseWithArgs<Args>(
        schedulersProvider: UseCaseSchedulersProvider
) : BaseCompletableUseCase(schedulersProvider) {

    protected abstract fun createCompletable(args: Args): Completable

    operator fun invoke(
            args: Args, applySchedulers: Boolean = true
    ): Completable = createCompletable(args).applySchedulersIfRequested(applySchedulers)
}