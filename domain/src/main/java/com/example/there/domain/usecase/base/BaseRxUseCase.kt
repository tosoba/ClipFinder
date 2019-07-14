package com.example.there.domain.usecase.base

import com.example.there.domain.UseCaseSchedulersProvider
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single

abstract class BaseRxUseCase(
        protected val schedulersProvider: UseCaseSchedulersProvider
) {
    protected fun <Result> Flowable<Result>.applySchedulersIfRequested(
            applySchedulers: Boolean
    ): Flowable<Result> = run {
        if (applySchedulers) this.subscribeOn(schedulersProvider.subscribeOnScheduler)
                .observeOn(schedulersProvider.observeOnScheduler)
        else this
    }

    protected fun <Result> Single<Result>.applySchedulersIfRequested(
            applySchedulers: Boolean
    ): Single<Result> = run {
        if (applySchedulers) this.subscribeOn(schedulersProvider.subscribeOnScheduler)
                .observeOn(schedulersProvider.observeOnScheduler)
        else this
    }

    protected fun <Result> Observable<Result>.applySchedulersIfRequested(
            applySchedulers: Boolean
    ): Observable<Result> = run {
        if (applySchedulers) this.subscribeOn(schedulersProvider.subscribeOnScheduler)
                .observeOn(schedulersProvider.observeOnScheduler)
        else this
    }

    protected fun Completable.applySchedulersIfRequested(
            applySchedulers: Boolean
    ): Completable = run {
        if (applySchedulers) this.subscribeOn(schedulersProvider.subscribeOnScheduler)
                .observeOn(schedulersProvider.observeOnScheduler)
        else this
    }
}