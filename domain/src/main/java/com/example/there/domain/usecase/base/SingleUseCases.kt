package com.example.there.domain.usecase.base

import com.example.core.ext.RetryStrategy
import com.example.core.ext.retry
import com.example.core.ext.RxSchedulers
import com.example.core.ext.applySchedulers
import io.reactivex.Single

abstract class SingleUseCase<Result>(private val schedulers: RxSchedulers) {
    protected abstract val result: Single<Result>

    operator fun invoke(
        applySchedulers: Boolean = true, strategy: RetryStrategy? = null
    ): Single<Result> = result
        .run { strategy?.let { retry(strategy) } ?: this }
        .run { if (applySchedulers) applySchedulers(schedulers) else this }
}

abstract class SingleUseCaseWithArgs<Args, Res>(private val schedulers: RxSchedulers) {
    protected abstract fun run(args: Args): Single<Res>

    operator fun invoke(
        args: Args, applySchedulers: Boolean = true, strategy: RetryStrategy? = null
    ): Single<Res> = run(args)
        .run { strategy?.let { retry(strategy) } ?: this }
        .run { if (applySchedulers) applySchedulers(schedulers) else this }
}
