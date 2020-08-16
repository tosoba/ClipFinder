package com.example.there.domain.usecase.base

import com.example.core.ext.*
import io.reactivex.Observable

abstract class ObservableUseCase<Result>(private val schedulers: RxSchedulers) {
    protected abstract val result: Observable<Result>

    operator fun invoke(
        applySchedulers: Boolean = true,
        timeout: Timeout = Timeout.DEFAULT,
        strategy: RetryStrategy? = null
    ): Observable<Result> = result
        .timeout(timeout.limit, timeout.unit)
        .run { strategy?.let { retry(strategy) } ?: this }
        .run { if (applySchedulers) applySchedulers(schedulers) else this }
}

abstract class ObservableUseCaseWithArgs<Args, Result>(private val schedulers: RxSchedulers) {
    protected abstract fun run(args: Args): Observable<Result>

    operator fun invoke(
        args: Args,
        applySchedulers: Boolean = true,
        timeout: Timeout = Timeout.DEFAULT,
        strategy: RetryStrategy? = null
    ): Observable<Result> = run(args)
        .timeout(timeout.limit, timeout.unit)
        .run { strategy?.let { retry(strategy) } ?: this }
        .run { if (applySchedulers) applySchedulers(schedulers) else this }
}
