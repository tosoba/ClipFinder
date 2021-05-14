package com.clipfinder.core.soundcloud.usecase

import com.clipfinder.core.ext.RxSchedulers
import com.clipfinder.core.model.UseCaseWithArgs
import com.clipfinder.core.model.invoke
import com.clipfinder.core.soundcloud.preferences.ISoundCloudPreferences
import io.reactivex.Single
import retrofit2.HttpException

abstract class AuthorizedSoundCloudUseCaseWithArgs<Args, Result>(
    private val getClientId: GetClientId,
    private val preferences: ISoundCloudPreferences,
    private val rxSchedulers: RxSchedulers
) : UseCaseWithArgs<Args, Single<Result>> {
    override fun run(args: Args): Single<Result> {
        val clientId = preferences.clientId
        return if (clientId != null) {
            getResourceWithClientId(args, clientId).subscribeOn(rxSchedulers.io).onErrorResumeNext {
                if (it is HttpException && it.code() == 401) {
                    getResourceWithNewClientId(args)
                } else {
                    Single.error(it)
                }
            }
        } else {
            getResourceWithNewClientId(args)
        }
    }

    private fun getResourceWithNewClientId(args: Args): Single<Result> =
        getClientId().flatMap { getResourceWithClientId(args, it) }

    protected abstract fun getResourceWithClientId(args: Args, clientId: String): Single<Result>
}
