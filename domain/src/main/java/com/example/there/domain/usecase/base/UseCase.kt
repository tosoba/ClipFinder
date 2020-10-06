package com.example.there.domain.usecase.base

import com.example.core.ext.*
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single

interface UseCase<Result> {
    val result: Result
}

interface UseCaseWithArgs<Args, Result> {
    fun run(args: Args): Result
}

operator fun UseCase<Completable>.invoke(
    timeout: Timeout = Timeout.DEFAULT,
    strategy: RetryStrategy? = null,
    schedulers: RxSchedulers? = null
): Completable = result
    .timeout(timeout.limit, timeout.unit)
    .run { strategy?.let(::retry) ?: this }
    .run { schedulers?.let(::applySchedulers) ?: this }

operator fun <Result> UseCase<Single<Result>>.invoke(
    timeout: Timeout = Timeout.DEFAULT,
    strategy: RetryStrategy? = null,
    schedulers: RxSchedulers? = null
): Single<Result> = result
    .timeout(timeout.limit, timeout.unit)
    .run { strategy?.let(::retry) ?: this }
    .run { schedulers?.let(::applySchedulers) ?: this }

operator fun <Result> UseCase<Observable<Result>>.invoke(
    timeout: Timeout = Timeout.DEFAULT,
    strategy: RetryStrategy? = null,
    schedulers: RxSchedulers? = null
): Observable<Result> = result
    .timeout(timeout.limit, timeout.unit)
    .run { strategy?.let(::retry) ?: this }
    .run { schedulers?.let(::applySchedulers) ?: this }

operator fun <Result> UseCase<Flowable<Result>>.invoke(
    timeout: Timeout = Timeout.DEFAULT,
    strategy: RetryStrategy? = null,
    schedulers: RxSchedulers? = null
): Flowable<Result> = result
    .timeout(timeout.limit, timeout.unit)
    .run { strategy?.let(::retry) ?: this }
    .run { schedulers?.let(::applySchedulers) ?: this }

operator fun <Args> UseCaseWithArgs<Args, Completable>.invoke(
    args: Args,
    timeout: Timeout = Timeout.DEFAULT,
    strategy: RetryStrategy? = null,
    schedulers: RxSchedulers? = null
): Completable = run(args)
    .timeout(timeout.limit, timeout.unit)
    .run { strategy?.let(::retry) ?: this }
    .run { schedulers?.let(::applySchedulers) ?: this }

operator fun <Args, Result> UseCaseWithArgs<Args, Single<Result>>.invoke(
    args: Args,
    timeout: Timeout = Timeout.DEFAULT,
    strategy: RetryStrategy? = null,
    schedulers: RxSchedulers? = null
): Single<Result> = run(args)
    .timeout(timeout.limit, timeout.unit)
    .run { strategy?.let(::retry) ?: this }
    .run { schedulers?.let(::applySchedulers) ?: this }

operator fun <Args, Result> UseCaseWithArgs<Args, Observable<Result>>.invoke(
    args: Args,
    timeout: Timeout = Timeout.DEFAULT,
    strategy: RetryStrategy? = null,
    schedulers: RxSchedulers? = null
): Observable<Result> = run(args)
    .timeout(timeout.limit, timeout.unit)
    .run { strategy?.let(::retry) ?: this }
    .run { schedulers?.let(::applySchedulers) ?: this }

operator fun <Args, Result> UseCaseWithArgs<Args, Flowable<Result>>.invoke(
    args: Args,
    timeout: Timeout = Timeout.DEFAULT,
    strategy: RetryStrategy? = null,
    schedulers: RxSchedulers? = null
): Flowable<Result> = run(args)
    .timeout(timeout.limit, timeout.unit)
    .run { strategy?.let(::retry) ?: this }
    .run { schedulers?.let(::applySchedulers) ?: this }
