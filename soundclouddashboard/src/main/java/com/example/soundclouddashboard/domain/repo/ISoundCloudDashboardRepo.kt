package com.example.soundclouddashboard.domain.repo

import com.clipfinder.core.soundcloud.model.ISoundCloudPlaylistSelection
import io.reactivex.Single

interface ISoundCloudDashboardRepo {
    val clientId: Single<String>
    fun mixedSelections(clientId: String): Single<List<ISoundCloudPlaylistSelection>>
}
