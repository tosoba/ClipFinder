package com.example.soundclouddashboard.data

import com.clipfinder.core.soundcloud.model.ISoundCloudPlaylistSelection
import com.clipfinder.soundcloud.api.SoundCloudApiV2
import com.clipfinder.soundcloud.api.model.mixed.selections.SoundCloudMixedSelectionsResponse
import com.example.soundclouddashboard.domain.repo.ISoundCloudDashboardRepo
import io.reactivex.Single

class SoundCloudDashboardRepo(
    private val apiV2: SoundCloudApiV2
) : ISoundCloudDashboardRepo {
    override fun mixedSelections(clientId: String): Single<List<ISoundCloudPlaylistSelection>> = apiV2
        .mixedSelections(clientId)
        .map(SoundCloudMixedSelectionsResponse::collection)
}
