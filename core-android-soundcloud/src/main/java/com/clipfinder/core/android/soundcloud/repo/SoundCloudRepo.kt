package com.clipfinder.core.android.soundcloud.repo

import com.clipfinder.core.soundcloud.model.ISoundCloudPlaylistSelection
import com.clipfinder.core.soundcloud.model.ISoundCloudTrack
import com.clipfinder.core.soundcloud.repo.ISoundCloudRepo
import com.clipfinder.soundcloud.api.SoundCloudApi
import com.clipfinder.soundcloud.api.SoundCloudApiV2
import com.clipfinder.soundcloud.api.model.collection.SoundCollectionResponse
import com.clipfinder.soundcloud.api.model.mixed.selections.SoundCloudMixedSelectionsResponse
import io.reactivex.Single

class SoundCloudRepo(private val apiV2: SoundCloudApiV2, private val api: SoundCloudApi) :
    ISoundCloudRepo {

    override fun mixedSelections(clientId: String): Single<List<ISoundCloudPlaylistSelection>> =
        apiV2.mixedSelections(clientId).map(SoundCloudMixedSelectionsResponse::collection)

    override fun featuredTracks(
        kind: String,
        genre: String,
        clientId: String
    ): Single<List<ISoundCloudTrack>> =
        apiV2.featuredTracks(kind, genre, clientId).map(SoundCollectionResponse::collection)

    override fun getTracksFromPlaylist(id: String): Single<List<ISoundCloudTrack>> =
        api.getTracksFromPlaylist(id).map { it }

    override fun getTracks(ids: List<String>): Single<List<ISoundCloudTrack>> =
        apiV2.getTracks(ids.joinToString(separator = ",")).map { it }

    override fun getSimilarTracks(id: String): Single<List<ISoundCloudTrack>> =
        apiV2.getRelatedTracks(id).map { it.collection ?: run(::emptyList) }
}
