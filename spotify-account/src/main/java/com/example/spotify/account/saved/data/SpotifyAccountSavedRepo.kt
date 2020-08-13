package com.example.spotify.account.saved.data

import com.example.core.SpotifyDefaults
import com.example.core.android.spotify.api.SpotifyAuth
import com.example.core.android.spotify.mapper.domain
import com.example.core.model.Paged
import com.example.core.model.Resource
import com.example.core.retrofit.mapToResource
import com.example.spotify.account.saved.domain.repo.ISpotifyAccountSavedRepo
import com.example.spotifyapi.SpotifyPersonalizationApi
import com.example.spotifyapi.util.domain
import com.example.there.domain.entity.spotify.AlbumEntity
import com.example.there.domain.entity.spotify.TrackEntity
import io.reactivex.Single

class SpotifyAccountSavedRepo(
    private val auth: SpotifyAuth,
    private val personalizationApi: SpotifyPersonalizationApi
) : ISpotifyAccountSavedRepo {

    override fun getCurrentUsersSavedTracks(
        offset: Int
    ): Single<Resource<Paged<List<TrackEntity>>>> = auth.withTokenSingle { token ->
        personalizationApi.getCurrentUsersSavedTracks(
            authorization = token,
            offset = offset
        ).mapToResource {
            Paged(
                contents = items.map { it.track.domain },
                offset = offset + SpotifyDefaults.LIMIT,
                total = totalItems
            )
        }
    }

    override fun getCurrentUsersSavedAlbums(
        offset: Int
    ): Single<Resource<Paged<List<AlbumEntity>>>> = auth.withTokenSingle { token ->
        personalizationApi.getCurrentUsersSavedAlbums(
            authorization = token,
            offset = offset
        ).mapToResource {
            Paged(
                contents = items.map { it.album.domain },
                offset = offset + SpotifyDefaults.LIMIT,
                total = totalItems
            )
        }
    }
}
