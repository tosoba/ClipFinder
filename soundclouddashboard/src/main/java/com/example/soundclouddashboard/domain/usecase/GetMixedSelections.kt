package com.example.soundclouddashboard.domain.usecase

import com.clipfinder.core.soundcloud.model.ISoundCloudPlaylistSelection
import com.clipfinder.core.soundcloud.preferences.ISoundCloudPreferences
import com.clipfinder.core.soundcloud.usecase.AuthorizedSoundCloudUseCase
import com.clipfinder.core.soundcloud.usecase.GetClientId
import com.example.core.ext.RxSchedulers
import com.example.core.model.Resource
import com.example.soundclouddashboard.domain.repo.ISoundCloudDashboardRepo
import io.reactivex.Single

class GetMixedSelections(
    getClientId: GetClientId,
    private val repo: ISoundCloudDashboardRepo,
    preferences: ISoundCloudPreferences,
    rxSchedulers: RxSchedulers
) : AuthorizedSoundCloudUseCase<Resource<List<ISoundCloudPlaylistSelection>>>(
    getClientId, preferences, rxSchedulers
) {
    override fun getResourceWithClientId(
        clientId: String
    ): Single<Resource<List<ISoundCloudPlaylistSelection>>> = repo
        .mixedSelections(clientId)
        .map { Resource.success(it) }
}
