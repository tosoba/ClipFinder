package com.example.spotify.search.data

import com.example.core.SpotifyDefaults
import com.example.core.android.spotify.api.SpotifyAuth
import com.example.core.android.spotify.mapper.domain
import com.example.core.model.Paged
import com.example.core.model.Resource
import com.example.core.retrofit.mapToResource
import com.example.spotifyapi.SpotifySearchApi
import com.example.spotifyapi.models.Album
import com.example.spotifyapi.models.Artist
import com.example.spotifyapi.models.Playlist
import com.example.spotifyapi.models.Track
import com.example.spotifyapi.util.domain
import com.example.spotify.search.domain.model.SpotifySearchResult
import com.example.spotify.search.domain.repo.ISpotifySearchRepo
import io.reactivex.Single

class SpotifySearchRepoImpl(
    private val searchApi: SpotifySearchApi,
    private val auth: SpotifyAuth
) : ISpotifySearchRepo {
    override fun search(
        query: String, offset: Int, type: String
    ): Single<Resource<SpotifySearchResult>> = auth.withTokenSingle { token ->
        searchApi.search(authorization = token, query = query, offset = offset, type = type)
            .mapToResource {
                SpotifySearchResult(
                    albums = albumsResult?.let {
                        Paged(it.items.map(Album::domain), it.offset + SpotifyDefaults.LIMIT, it.totalItems)
                    },
                    artists = artistsResult?.let {
                        Paged(it.items.map(Artist::domain), it.offset + SpotifyDefaults.LIMIT, it.totalItems)
                    },
                    playlists = playlistsResult?.let {
                        Paged(it.items.map(Playlist::domain), it.offset + SpotifyDefaults.LIMIT, it.totalItems)
                    },
                    tracks = tracksResult?.let {
                        Paged(it.items.map(Track::domain), it.offset + SpotifyDefaults.LIMIT, it.totalItems)
                    }
                )
            }
    }
}
