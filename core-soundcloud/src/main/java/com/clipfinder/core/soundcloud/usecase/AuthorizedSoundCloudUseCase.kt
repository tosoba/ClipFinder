package com.clipfinder.core.soundcloud.usecase

import com.clipfinder.core.ext.RxSchedulers
import com.clipfinder.core.ext.Timeout
import com.clipfinder.core.model.UseCase
import com.clipfinder.core.model.invoke
import com.clipfinder.core.soundcloud.preferences.ISoundCloudPreferences
import io.reactivex.Single
import retrofit2.HttpException
import java.util.concurrent.TimeUnit

abstract class AuthorizedSoundCloudUseCase<T>(
    private val getClientId: GetClientId,
    private val preferences: ISoundCloudPreferences,
    private val rxSchedulers: RxSchedulers
) : UseCase<Single<T>> {

    override val result: Single<T>
        get() {
            val clientId = preferences.clientId
            return when {
                clientId != null ->
                    getResourceWithClientId(clientId)
                        .subscribeOn(rxSchedulers.io)
                        .onErrorResumeNext {
                            if (it is HttpException && it.code() == 401) resourceWithNewClientId
                            else Single.error(it)
                        }
                else -> resourceWithNewClientId
            }
        }

    private val resourceWithNewClientId: Single<T>
        get() =
            getClientId(timeout = Timeout(25L, TimeUnit.SECONDS)).flatMap(::getResourceWithClientId)

    protected abstract fun getResourceWithClientId(clientId: String): Single<T>
}
