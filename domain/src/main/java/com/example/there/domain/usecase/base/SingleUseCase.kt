package com.example.there.domain.usecase.base

import com.example.there.domain.UseCaseSchedulersProvider
import io.reactivex.Single

abstract class BaseSingleUseCase<Result>(
        schedulersProvider: UseCaseSchedulersProvider
) : BaseRxUseCase(schedulersProvider) {

    protected fun Single<Result>.applySchedulersIfRequested(
            applySchedulers: Boolean
    ): Single<Result> = run {
        if (applySchedulers) this.subscribeOn(schedulersProvider.subscribeOnScheduler)
                .observeOn(schedulersProvider.observeOnScheduler)
        else this
    }
}

abstract class SingleUseCase<Result>(
        schedulersProvider: UseCaseSchedulersProvider
) : BaseSingleUseCase<Result>(schedulersProvider) {

    protected abstract val single: Single<Result>

    operator fun invoke(
            applySchedulers: Boolean = true
    ): Single<Result> = single.applySchedulersIfRequested(applySchedulers)
}

abstract class SingleUseCaseWithArgs<Args, Result>(
        schedulersProvider: UseCaseSchedulersProvider
) : BaseSingleUseCase<Result>(schedulersProvider) {

    protected abstract fun createSingle(args: Args): Single<Result>

    operator fun invoke(
            args: Args, applySchedulers: Boolean = true
    ): Single<Result> = createSingle(args).applySchedulersIfRequested(applySchedulers)
}