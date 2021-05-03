package com.clipfinder.core.soundcloud.usecase

import com.clipfinder.core.ext.RxSchedulers
import com.clipfinder.core.model.Resource
import com.clipfinder.core.soundcloud.model.ISoundCloudTrack
import com.clipfinder.core.soundcloud.preferences.ISoundCloudPreferences
import com.clipfinder.core.soundcloud.repo.ISoundCloudRepo
import io.reactivex.Single

class GetFeaturedTracks(
    getClientId: GetClientId,
    private val repo: ISoundCloudRepo,
    preferences: ISoundCloudPreferences,
    rxSchedulers: RxSchedulers
) :
    AuthorizedSoundCloudUseCase<Resource<List<ISoundCloudTrack>>>(
        getClientId,
        preferences,
        rxSchedulers
    ) {
    override fun getResourceWithClientId(
        clientId: String
    ): Single<Resource<List<ISoundCloudTrack>>> =
        repo.featuredTracks(kind = "top", genre = "all-music", clientId = clientId).map {
            Resource.success(it)
        }
}
