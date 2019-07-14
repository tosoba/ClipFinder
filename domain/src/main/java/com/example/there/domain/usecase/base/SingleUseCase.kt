package com.example.there.domain.usecase.base

import com.example.core.model.Resource
import com.example.core.retrofit.ThrowableServerError
import com.example.there.domain.UseCaseSchedulersProvider
import io.reactivex.Single


abstract class SingleUseCase<Result>(
        schedulersProvider: UseCaseSchedulersProvider
) : BaseRxUseCase(schedulersProvider) {

    protected abstract val result: Single<Result>

    operator fun invoke(
            applySchedulers: Boolean = true
    ): Single<Result> = result.applySchedulersIfRequested(applySchedulers)
}

abstract class SingleUseCaseWithArgs<Args, Res>(
        schedulersProvider: UseCaseSchedulersProvider
) : BaseRxUseCase(schedulersProvider) {

    protected abstract fun run(args: Args): Single<Res>

    operator fun invoke(
            args: Args, applySchedulers: Boolean = true
    ): Single<Res> = run(args).applySchedulersIfRequested(applySchedulers)
}

operator fun <Res> SingleUseCase<Resource<Res>>.invoke(
        applySchedulers: Boolean = true,
        wrapErrors: Boolean = true
): Single<Resource<Res>> = invoke(applySchedulers).run {
    if (wrapErrors) {
        onErrorResumeNext { throwable: Throwable ->
            when (throwable) {
                is ThrowableServerError -> Single.just(Resource.Error(throwable.error))
                else -> Single.just(Resource.Error(throwable))
            }
        }
    } else this
}

sealed class ErrorHandling {
    object Wrap : ErrorHandling()
    object Throw : ErrorHandling()
}
