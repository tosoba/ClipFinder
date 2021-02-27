package com.clipfinder.core.soundcloud.usecase

import com.clipfinder.core.soundcloud.auth.ISoundCloudAuth
import com.clipfinder.core.soundcloud.preferences.ISoundCloudPreferences
import com.clipfinder.core.model.UseCase
import io.reactivex.Single

class GetClientId(
    private val auth: ISoundCloudAuth,
    private val preferences: ISoundCloudPreferences
) : UseCase<Single<String>> {
    override val result: Single<String>
        get() = auth.clientId.doOnSuccess { preferences.clientId = it }
}
