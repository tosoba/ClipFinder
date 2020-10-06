package com.clipfinder.core.soundcloud.usecase

import com.clipfinder.core.soundcloud.preferences.ISoundCloudPreferences
import com.example.core.ext.RxSchedulers
import com.example.there.domain.usecase.base.SingleUseCase
import io.reactivex.Single
import retrofit2.HttpException

abstract class AuthorizedSoundCloudUseCase<T>(
    private val getClientId: GetClientId,
    private val preferences: ISoundCloudPreferences,
    private val rxSchedulers: RxSchedulers
) : SingleUseCase<T>(rxSchedulers) {
    override val result: Single<T>
        get() {
            val clientId = preferences.clientId
            return if (clientId != null) {
                getResourceWithClientId(clientId)
                    .subscribeOn(rxSchedulers.io)
                    .onErrorResumeNext {
                        if (it is HttpException && it.code() == 401) resourceWithNewClientId
                        else Single.error(it)
                    }
            } else {
                resourceWithNewClientId
            }
        }

    private val resourceWithNewClientId: Single<T>
        get() = getClientId(applySchedulers = false)
            .subscribeOn(rxSchedulers.main)
            .observeOn(rxSchedulers.io)
            .flatMap(::getResourceWithClientId)

    protected abstract fun getResourceWithClientId(clientId: String): Single<T>
}