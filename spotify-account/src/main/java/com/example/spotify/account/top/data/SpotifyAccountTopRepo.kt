package com.example.spotify.account.top.data

import com.example.core.SpotifyDefaults
import com.example.core.android.spotify.api.SpotifyAuth
import com.example.core.android.spotify.mapper.domain
import com.example.core.model.Paged
import com.example.core.model.Resource
import com.example.core.retrofit.mapToResource
import com.example.spotify.account.top.domain.repo.ISpotifyAccountTopRepo
import com.example.spotifyapi.SpotifyPersonalizationApi
import com.example.spotifyapi.models.Artist
import com.example.spotifyapi.models.Track
import com.example.spotifyapi.util.domain
import com.example.there.domain.entity.spotify.ArtistEntity
import com.example.there.domain.entity.spotify.TrackEntity
import io.reactivex.Single

class SpotifyAccountTopRepo(
    private val auth: SpotifyAuth,
    private val personalizationApi: SpotifyPersonalizationApi
) : ISpotifyAccountTopRepo {

    override fun getCurrentUsersTopTracks(
        offset: Int
    ): Single<Resource<Paged<List<TrackEntity>>>> = auth.withTokenSingle(true) { token ->
        personalizationApi.getCurrentUsersTopTracks(authorization = token, offset = offset)
            .mapToResource {
                Paged(
                    contents = items.map(Track::domain),
                    offset = offset + SpotifyDefaults.LIMIT,
                    total = totalItems
                )
            }
    }

    override fun getCurrentUsersTopArtists(
        offset: Int
    ): Single<Resource<Paged<List<ArtistEntity>>>> = auth.withTokenSingle(true) { token ->
        personalizationApi.getCurrentUsersTopArtists(authorization = token, offset = offset)
            .mapToResource {
                Paged(
                    contents = items.map(Artist::domain),
                    offset = offset + SpotifyDefaults.LIMIT,
                    total = totalItems
                )
            }
    }
}