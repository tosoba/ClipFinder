package com.example.spotify.album.data

import com.clipfinder.core.spotify.model.ISpotifyTrack
import com.clipfinder.spotify.api.endpoint.AlbumEndpoints
import com.clipfinder.spotify.api.endpoint.TracksEndpoints
import com.example.core.SpotifyDefaults
import com.example.core.model.Paged
import com.example.core.model.Resource
import com.example.core.retrofit.mapSuccess
import com.example.core.retrofit.mapToResource
import com.example.spotify.album.domain.repo.ISpotifyAlbumRepo
import io.reactivex.Single

class SpotifyAlbumRepo(
    private val albumEndpoints: AlbumEndpoints,
    private val tracksEndpoints: TracksEndpoints
) : ISpotifyAlbumRepo {

    override fun getTracksFromAlbum(
        albumId: String, offset: Int
    ): Single<Resource<Paged<List<ISpotifyTrack>>>> = albumEndpoints
        .getAnAlbumsTracks(id = albumId, offset = offset)
        .mapSuccess {
            Paged(
                contents = items.joinToString(separator = ",") { it.id },
                offset = offset,
                total = total
            )
        }
        .flatMap { idsPage ->
            tracksEndpoints
                .getSeveralTracks(ids = idsPage.contents)
                .mapToResource {
                    Paged<List<ISpotifyTrack>>(
                        contents = tracks,
                        offset = idsPage.offset + SpotifyDefaults.LIMIT,
                        total = idsPage.total
                    )
                }
        }

}
