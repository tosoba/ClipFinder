package com.example.core.android.spotify.repo

import com.clipfinder.core.spotify.model.ISpotifyArtist
import com.clipfinder.core.spotify.model.ISpotifySimplifiedAlbum
import com.clipfinder.core.spotify.model.ISpotifyTrack
import com.clipfinder.core.spotify.repo.ISpotifyRepo
import com.clipfinder.spotify.api.endpoint.AlbumEndpoints
import com.clipfinder.spotify.api.endpoint.ArtistEndpoints
import com.clipfinder.spotify.api.endpoint.BrowseEndpoints
import com.clipfinder.spotify.api.endpoint.TracksEndpoints
import com.example.core.SpotifyDefaults
import com.example.core.model.Paged
import com.example.core.model.Resource
import com.example.core.retrofit.mapSuccess
import com.example.core.retrofit.mapToResource
import io.reactivex.Single

class SpotifyRepo(
    private val albumEndpoints: AlbumEndpoints,
    private val artistEndpoints: ArtistEndpoints,
    private val browseEndpoints: BrowseEndpoints,
    private val tracksEndpoints: TracksEndpoints
) : ISpotifyRepo {

    override fun getAlbum(id: String): Single<Resource<ISpotifySimplifiedAlbum>> = albumEndpoints
        .getAnAlbum(id = id)
        .mapToResource { this }

    override fun getArtists(ids: List<String>): Single<Resource<List<ISpotifyArtist>>> = artistEndpoints
        .getMultipleArtists(ids = ids.joinToString(separator = ","))
        .mapToResource { artists }

    override fun getSimilarTracks(
        id: String, offset: Int
    ): Single<Resource<Paged<List<ISpotifyTrack>>>> = browseEndpoints
        .getRecommendations(seedTracks = id, limit = 100)
        .mapSuccess {
            tracks.chunked(SpotifyDefaults.LIMIT)
                .map { it.joinToString(",") { track -> track.id } }
        }
        .flatMap { chunks ->
            val chunk = chunks[offset]
            tracksEndpoints.getSeveralTracks(ids = chunk)
                .mapToResource { Paged<List<ISpotifyTrack>>(tracks, offset + 1, chunks.size) }
        }

}