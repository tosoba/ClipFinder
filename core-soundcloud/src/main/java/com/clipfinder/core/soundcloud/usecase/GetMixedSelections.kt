package com.clipfinder.core.soundcloud.usecase

import com.clipfinder.core.ext.RxSchedulers
import com.clipfinder.core.model.Resource
import com.clipfinder.core.soundcloud.model.ISoundCloudPlaylistSelection
import com.clipfinder.core.soundcloud.preferences.ISoundCloudPreferences
import com.clipfinder.core.soundcloud.repo.ISoundCloudRepo
import io.reactivex.Single

class GetMixedSelections(
    getClientId: GetClientId,
    private val repo: ISoundCloudRepo,
    preferences: ISoundCloudPreferences,
    rxSchedulers: RxSchedulers
) : AuthorizedSoundCloudUseCase<Resource<List<ISoundCloudPlaylistSelection>>>(getClientId, preferences, rxSchedulers) {
    override fun getResourceWithClientId(clientId: String): Single<Resource<List<ISoundCloudPlaylistSelection>>> = repo
        .mixedSelections(clientId)
        .map { Resource.success(it) }
}
