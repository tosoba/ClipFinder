package com.example.spotify.artist.data

import com.example.core.SpotifyDefaults
import com.example.core.android.spotify.api.SpotifyAuth
import com.example.core.android.spotify.mapper.domain
import com.example.core.android.spotify.preferences.SpotifyPreferences
import com.example.core.model.Paged
import com.example.core.model.Resource
import com.example.core.retrofit.mapToResource
import com.example.spotify.artist.domain.repo.ISpotifyArtistRepo
import com.example.spotifyapi.SpotifyArtistsApi
import com.example.spotifyapi.models.Artist
import com.example.spotifyapi.models.SimpleAlbum
import com.example.spotifyapi.models.Track
import com.example.spotifyapi.util.domain
import com.example.there.domain.entity.spotify.AlbumEntity
import com.example.there.domain.entity.spotify.ArtistEntity
import com.example.there.domain.entity.spotify.TrackEntity
import io.reactivex.Single

class SpotifyArtistRepo(
    private val artistsApi: SpotifyArtistsApi,
    private val auth: SpotifyAuth,
    private val preferences: SpotifyPreferences
) : ISpotifyArtistRepo {

    override fun getAlbumsFromArtist(
        artistId: String,
        offset: Int
    ): Single<Resource<Paged<List<AlbumEntity>>>> = auth.withTokenSingle { token ->
        artistsApi.getArtistsAlbums(
            authorization = token,
            id = artistId,
            offset = offset
        ).mapToResource {
            Paged(
                contents = items.map(SimpleAlbum::domain),
                offset = this.offset + SpotifyDefaults.LIMIT,
                total = total
            )
        }
    }

    override fun getRelatedArtists(
        artistId: String
    ): Single<Resource<List<ArtistEntity>>> = auth.withTokenSingle { token ->
        artistsApi.getArtistsRelatedArtists(
            authorization = token,
            id = artistId
        ).mapToResource { result.map(Artist::domain) }
    }

    override fun getTopTracksFromArtist(
        artistId: String
    ): Single<Resource<List<TrackEntity>>> = auth.withTokenSingle { token ->
        artistsApi.getArtistsTopTracks(
            authorization = token,
            id = artistId,
            country = preferences.country
        ).mapToResource { result.map(Track::domain) }
    }
}
