package com.example.there.domain.usecase.base

import com.example.core.ext.*
import io.reactivex.Flowable

abstract class FlowableUseCase<Result>(private val schedulers: RxSchedulers) {
    protected abstract val result: Flowable<Result>

    operator fun invoke(
        applySchedulers: Boolean = true,
        timeout: Timeout = Timeout.DEFAULT,
        strategy: RetryStrategy? = null
    ): Flowable<Result> = result
        .timeout(timeout.limit, timeout.unit)
        .run { strategy?.let { retry(it) } ?: this }
        .run { if (applySchedulers) applySchedulers(schedulers) else this }
}

abstract class FlowableUseCaseWithArgs<Args, Result>(private val schedulers: RxSchedulers) {
    protected abstract fun run(args: Args): Flowable<Result>

    operator fun invoke(
        args: Args,
        applySchedulers: Boolean = true,
        timeout: Timeout = Timeout.DEFAULT,
        strategy: RetryStrategy? = null
    ): Flowable<Result> = run(args)
        .timeout(timeout.limit, timeout.unit)
        .run { strategy?.let { retry(it) } ?: this }
        .run { if (applySchedulers) applySchedulers(schedulers) else this }
}
