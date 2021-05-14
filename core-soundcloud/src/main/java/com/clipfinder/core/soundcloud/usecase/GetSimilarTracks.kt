package com.clipfinder.core.soundcloud.usecase

import com.clipfinder.core.ext.RxSchedulers
import com.clipfinder.core.soundcloud.model.ISoundCloudTrack
import com.clipfinder.core.soundcloud.preferences.ISoundCloudPreferences
import com.clipfinder.core.soundcloud.repo.ISoundCloudRepo
import io.reactivex.Single

class GetSimilarTracks(
    getClientId: GetClientId,
    private val repo: ISoundCloudRepo,
    preferences: ISoundCloudPreferences,
    rxSchedulers: RxSchedulers
) :
    AuthorizedSoundCloudUseCaseWithArgs<String, List<ISoundCloudTrack>>(
        getClientId,
        preferences,
        rxSchedulers
    ) {
    override fun getResourceWithClientId(
        args: String,
        clientId: String
    ): Single<List<ISoundCloudTrack>> = repo.getSimilarTracks(args, clientId)
}
