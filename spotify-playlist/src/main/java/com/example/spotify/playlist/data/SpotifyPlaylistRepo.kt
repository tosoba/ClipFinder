package com.example.spotify.playlist.data

import com.clipfinder.core.spotify.model.ISpotifyTrack
import com.clipfinder.spotify.api.endpoint.PlaylistsEndpoints
import com.clipfinder.spotify.api.model.TrackObject
import com.example.core.SpotifyDefaults
import com.example.core.model.Paged
import com.example.core.model.Resource
import com.example.core.retrofit.mapToResource
import com.example.spotify.playlist.domain.repo.ISpotifyPlaylistRepo
import io.reactivex.Single

class SpotifyPlaylistRepo(
    private val playlistsEndpoints: PlaylistsEndpoints
) : ISpotifyPlaylistRepo {

    override fun getPlaylistTracks(
        playlistId: String, offset: Int
    ): Single<Resource<Paged<List<ISpotifyTrack>>>> = playlistsEndpoints
        .getPlaylistsTracks(playlistId = playlistId, offset = offset)
        .mapToResource {
            Paged<List<ISpotifyTrack>>(
                contents = items.filterIsInstance<TrackObject>(),
                offset = offset + SpotifyDefaults.LIMIT,
                total = total
            )
        }
}
