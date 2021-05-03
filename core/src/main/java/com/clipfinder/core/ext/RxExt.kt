package com.clipfinder.core.ext

import io.reactivex.*
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import java.util.concurrent.TimeUnit

interface RxSchedulers {
    val io: Scheduler
    val main: Scheduler
}

fun <T> Flowable<T>.applySchedulers(schedulers: RxSchedulers): Flowable<T> =
    subscribeOn(schedulers.io).observeOn(schedulers.main)

fun <T> Single<T>.applySchedulers(schedulers: RxSchedulers): Single<T> =
    subscribeOn(schedulers.io).observeOn(schedulers.main)

fun <T> Observable<T>.applySchedulers(schedulers: RxSchedulers): Observable<T> =
    subscribeOn(schedulers.io).observeOn(schedulers.main)

fun Completable.applySchedulers(schedulers: RxSchedulers): Completable =
    subscribeOn(schedulers.io).observeOn(schedulers.main)

class Timeout(val limit: Long, val unit: TimeUnit) {
    companion object {
        val DEFAULT = Timeout(15, TimeUnit.SECONDS)
    }
}

fun Disposable.addTo(compositeDisposable: CompositeDisposable): Boolean =
    compositeDisposable.add(this)
