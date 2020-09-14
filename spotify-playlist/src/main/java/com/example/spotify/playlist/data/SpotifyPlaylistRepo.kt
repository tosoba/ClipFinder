package com.example.spotify.playlist.data

import com.example.core.SpotifyDefaults
import com.example.core.android.spotify.api.SpotifyAuth
import com.example.core.model.Paged
import com.example.core.model.Resource
import com.example.core.retrofit.mapToResource
import com.example.spotifyapi.util.domain
import com.example.spotify.playlist.domain.repo.ISpotifyPlaylistRepo
import com.example.spotifyapi.SpotifyPlaylistsApi
import com.example.there.domain.entity.spotify.TrackEntity
import io.reactivex.Single

class SpotifyPlaylistRepo(
    private val auth: SpotifyAuth,
    private val playlistsApi: SpotifyPlaylistsApi
) : ISpotifyPlaylistRepo {

    override fun getPlaylistTracks(
        playlistId: String,
        offset: Int
    ): Single<Resource<Paged<List<TrackEntity>>>> = auth.withTokenSingle { token ->
        playlistsApi.getPlaylistTracks(
            authorization = token,
            playlistId = playlistId,
            offset = offset
        ).mapToResource {
            Paged(
                contents = items.map { it.track.domain },
                offset = offset + SpotifyDefaults.LIMIT,
                total = totalItems
            )
        }
    }
}
