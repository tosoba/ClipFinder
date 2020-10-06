package com.clipfinder.core.soundcloud.usecase

import com.clipfinder.core.soundcloud.api.ISoundCloudAuth
import com.clipfinder.core.soundcloud.preferences.ISoundCloudPreferences
import com.example.core.ext.RxSchedulers
import com.example.there.domain.usecase.base.SingleUseCase
import io.reactivex.Single

class GetClientId(
    private val auth: ISoundCloudAuth,
    private val preferences: ISoundCloudPreferences,
    rxSchedulers: RxSchedulers
) : SingleUseCase<String>(rxSchedulers) {
    override val result: Single<String>
        get() = auth.clientId.doOnSuccess { preferences.clientId = it }
}
