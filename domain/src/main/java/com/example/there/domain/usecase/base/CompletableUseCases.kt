package com.example.there.domain.usecase.base

import com.example.core.ext.RetryStrategy
import com.example.core.ext.retry
import com.example.core.ext.RxSchedulers
import com.example.core.ext.applySchedulers
import io.reactivex.Completable

abstract class CompletableUseCase(private val schedulers: RxSchedulers) {
    protected abstract val result: Completable

    operator fun invoke(
        applySchedulers: Boolean = true,
        strategy: RetryStrategy? = null
    ): Completable = result
        .run { strategy?.let { retry(strategy) } ?: this }
        .run { if (applySchedulers) applySchedulers(schedulers) else this }
}

abstract class CompletableUseCaseWithArgs<Args>(private val schedulers: RxSchedulers) {
    protected abstract fun run(args: Args): Completable

    operator fun invoke(
        args: Args,
        applySchedulers: Boolean = true,
        strategy: RetryStrategy? = null
    ): Completable = run(args)
        .run { strategy?.let { retry(strategy) } ?: this }
        .run { if (applySchedulers) applySchedulers(schedulers) else this }
}
