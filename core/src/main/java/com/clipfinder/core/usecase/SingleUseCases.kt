package com.clipfinder.core.usecase

import com.clipfinder.core.ext.*
import io.reactivex.Single

abstract class SingleUseCase<Result>(private val schedulers: RxSchedulers) {
    protected abstract val result: Single<Result>

    operator fun invoke(
        applySchedulers: Boolean = true,
        timeout: Timeout = Timeout.DEFAULT,
        strategy: RetryStrategy? = null
    ): Single<Result> = result
        .timeout(timeout.limit, timeout.unit)
        .run { strategy?.let(::retry) ?: this }
        .run { if (applySchedulers) applySchedulers(schedulers) else this }
}

abstract class SingleUseCaseWithArgs<Args, Res>(private val schedulers: RxSchedulers) {
    protected abstract fun run(args: Args): Single<Res>

    operator fun invoke(
        args: Args,
        applySchedulers: Boolean = true,
        timeout: Timeout = Timeout.DEFAULT,
        strategy: RetryStrategy? = null
    ): Single<Res> = run(args)
        .timeout(timeout.limit, timeout.unit)
        .run { strategy?.let(::retry) ?: this }
        .run { if (applySchedulers) applySchedulers(schedulers) else this }
}
