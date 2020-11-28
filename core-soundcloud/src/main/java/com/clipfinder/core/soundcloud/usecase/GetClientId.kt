package com.clipfinder.core.soundcloud.usecase

import com.clipfinder.core.ext.RxSchedulers
import com.clipfinder.core.soundcloud.auth.ISoundCloudAuth
import com.clipfinder.core.soundcloud.preferences.ISoundCloudPreferences
import com.clipfinder.core.usecase.SingleUseCase
import io.reactivex.Single

class GetClientId(
    private val auth: ISoundCloudAuth,
    private val preferences: ISoundCloudPreferences,
    rxSchedulers: RxSchedulers
) : SingleUseCase<String>(rxSchedulers) {
    override val result: Single<String>
        get() = auth.clientId.doOnSuccess { preferences.clientId = it }
}
