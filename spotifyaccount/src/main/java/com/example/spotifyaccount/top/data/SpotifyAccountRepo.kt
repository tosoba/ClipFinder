package com.example.spotifyaccount.top.data

import com.example.core.SpotifyDefaults
import com.example.core.android.spotify.api.SpotifyAuth
import com.example.core.android.spotify.mapper.domain
import com.example.core.model.Paged
import com.example.core.model.Resource
import com.example.core.retrofit.mapToResource
import com.example.spotifyaccount.top.domain.repo.ISpotifyAccountRepo
import com.example.spotifyapi.SpotifyPersonalizationApi
import com.example.spotifyapi.models.Artist
import com.example.spotifyapi.models.Track
import com.example.spotifyapi.util.domain
import com.example.there.domain.entity.spotify.ArtistEntity
import com.example.there.domain.entity.spotify.TrackEntity
import io.reactivex.Single

class SpotifyAccountRepo(
    private val auth: SpotifyAuth,
    private val personalizationApi: SpotifyPersonalizationApi
) : ISpotifyAccountRepo {

    override fun getCurrentUsersTopTracks(
        offset: Int
    ): Single<Resource<Paged<List<TrackEntity>>>> = auth.withTokenSingle { token ->
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
    ): Single<Resource<Paged<List<ArtistEntity>>>> = auth.withTokenSingle { token ->
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