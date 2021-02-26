package com.clipfinder.core.usecase

import com.clipfinder.core.ext.*
import io.reactivex.Completable

abstract class CompletableUseCase(private val schedulers: RxSchedulers) {
    protected abstract val result: Completable

    operator fun invoke(
        applySchedulers: Boolean = false,
        timeout: Timeout = Timeout.DEFAULT,
        strategy: RetryStrategy? = null
    ): Completable = result
        .timeout(timeout.limit, timeout.unit)
        .run { strategy?.let(::retry) ?: this }
        .run { if (applySchedulers) applySchedulers(schedulers) else this }
}

abstract class CompletableUseCaseWithArgs<Args>(private val schedulers: RxSchedulers) {
    protected abstract fun run(args: Args): Completable

    operator fun invoke(
        args: Args,
        applySchedulers: Boolean = false,
        timeout: Timeout = Timeout.DEFAULT,
        strategy: RetryStrategy? = null
    ): Completable = run(args)
        .timeout(timeout.limit, timeout.unit)
        .run { strategy?.let(::retry) ?: this }
        .run { if (applySchedulers) applySchedulers(schedulers) else this }
}
