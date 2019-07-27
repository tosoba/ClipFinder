package com.example.there.domain.usecase.base

import com.example.core.ext.RetryStrategy
import com.example.core.ext.retry
import com.example.there.domain.UseCaseSchedulersProvider
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single

abstract class BaseRxUseCase(
        protected val schedulersProvider: UseCaseSchedulersProvider
) {
    protected fun <T> Flowable<T>.applySchedulersIfRequested(
            applySchedulers: Boolean
    ): Flowable<T> = run {
        if (applySchedulers) this.subscribeOn(schedulersProvider.subscribeOnScheduler)
                .observeOn(schedulersProvider.observeOnScheduler)
        else this
    }

    protected fun <T> Single<T>.applySchedulersIfRequested(
            applySchedulers: Boolean
    ): Single<T> = run {
        if (applySchedulers) this.subscribeOn(schedulersProvider.subscribeOnScheduler)
                .observeOn(schedulersProvider.observeOnScheduler)
        else this
    }

    protected fun <T> Observable<T>.applySchedulersIfRequested(
            applySchedulers: Boolean
    ): Observable<T> = run {
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

    protected fun <T> Flowable<T>.retryIfNeeded(strategy: RetryStrategy?): Flowable<T> = run {
        strategy?.let { retry(strategy) } ?: this
    }

    protected fun <T> Single<T>.retryIfNeeded(strategy: RetryStrategy?): Single<T> = run {
        strategy?.let { retry(strategy) } ?: this
    }

    protected fun <T> Observable<T>.retryIfNeeded(strategy: RetryStrategy?): Observable<T> = run {
        strategy?.let { retry(strategy) } ?: this
    }

    protected fun Completable.retryIfNeeded(strategy: RetryStrategy?): Completable = run {
        strategy?.let { retry(strategy) } ?: this
    }
}