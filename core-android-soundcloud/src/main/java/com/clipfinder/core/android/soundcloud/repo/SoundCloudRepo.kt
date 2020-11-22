package com.clipfinder.core.android.soundcloud.repo

import com.clipfinder.core.soundcloud.model.ISoundCloudPlaylistSelection
import com.clipfinder.core.soundcloud.repo.ISoundCloudRepo
import com.clipfinder.soundcloud.api.SoundCloudApiV2
import com.clipfinder.soundcloud.api.model.mixed.selections.SoundCloudMixedSelectionsResponse
import io.reactivex.Single

class SoundCloudRepo(private val apiV2: SoundCloudApiV2) : ISoundCloudRepo {
    override fun mixedSelections(clientId: String): Single<List<ISoundCloudPlaylistSelection>> = apiV2
        .mixedSelections(clientId)
        .map(SoundCloudMixedSelectionsResponse::collection)
}