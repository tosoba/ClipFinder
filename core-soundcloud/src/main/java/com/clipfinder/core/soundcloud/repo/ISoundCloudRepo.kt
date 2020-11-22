package com.clipfinder.core.soundcloud.repo

import com.clipfinder.core.soundcloud.model.ISoundCloudPlaylistSelection
import io.reactivex.Single

interface ISoundCloudRepo {
    fun mixedSelections(clientId: String): Single<List<ISoundCloudPlaylistSelection>>
}