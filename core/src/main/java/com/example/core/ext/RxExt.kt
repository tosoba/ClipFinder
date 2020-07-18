package com.example.core.ext

import io.reactivex.*
import io.reactivex.disposables.Disposable

fun <T> Maybe<T>.isPresent(): Single<Boolean> = map { true }
    .defaultIfEmpty(false)
    .toSingle()

fun Disposable.disposeIfNeeded() {
    if (!isDisposed) dispose()
}

interface RxSchedulers {
    val subscribeOnScheduler: Scheduler
    val observeOnScheduler: Scheduler
}

fun <T> Flowable<T>.applySchedulers(
    schedulers: RxSchedulers
): Flowable<T> = subscribeOn(schedulers.subscribeOnScheduler)
    .observeOn(schedulers.observeOnScheduler)

fun <T> Single<T>.applySchedulers(
    schedulers: RxSchedulers
): Single<T> = subscribeOn(schedulers.subscribeOnScheduler)
    .observeOn(schedulers.observeOnScheduler)

fun <T> Observable<T>.applySchedulers(
    schedulers: RxSchedulers
): Observable<T> = subscribeOn(schedulers.subscribeOnScheduler)
    .observeOn(schedulers.observeOnScheduler)

fun Completable.applySchedulers(
    schedulers: RxSchedulers
): Completable = subscribeOn(schedulers.subscribeOnScheduler)
    .observeOn(schedulers.observeOnScheduler)
