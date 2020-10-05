package com.example.spotify.search.data

import com.clipfinder.core.spotify.model.ISpotifyArtist
import com.clipfinder.core.spotify.model.ISpotifySimplifiedAlbum
import com.clipfinder.core.spotify.model.ISpotifySimplifiedPlaylist
import com.clipfinder.core.spotify.model.ISpotifyTrack
import com.clipfinder.spotify.api.endpoint.SearchEndpoints
import com.example.core.SpotifyDefaults
import com.example.core.model.Paged
import com.example.core.model.Resource
import com.example.core.retrofit.mapToResource
import com.example.spotify.search.domain.model.SpotifySearchResult
import com.example.spotify.search.domain.repo.ISpotifySearchRepo
import io.reactivex.Single

class SpotifySearchRepoImpl(private val searchEndpoints: SearchEndpoints) : ISpotifySearchRepo {
    override fun search(
        query: String, offset: Int, type: String
    ): Single<Resource<SpotifySearchResult>> = searchEndpoints
        .search(q = query, offset = offset, type = type)
        .mapToResource {
            SpotifySearchResult(
                albums = album?.let {
                    Paged<List<ISpotifySimplifiedAlbum>>(it.items, it.offset + SpotifyDefaults.LIMIT, it.total)
                },
                artists = artist?.let {
                    Paged<List<ISpotifyArtist>>(it.items, it.offset + SpotifyDefaults.LIMIT, it.total)
                },
                playlists = playlist?.let {
                    Paged<List<ISpotifySimplifiedPlaylist>>(it.items, it.offset + SpotifyDefaults.LIMIT, it.total)
                },
                tracks = track?.let {
                    Paged<List<ISpotifyTrack>>(it.items, it.offset + SpotifyDefaults.LIMIT, it.total)
                }
            )
        }
}
