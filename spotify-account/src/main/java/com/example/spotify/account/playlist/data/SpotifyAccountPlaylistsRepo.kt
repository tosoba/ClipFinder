package com.example.spotify.account.playlist.data

import com.clipfinder.core.spotify.model.ISpotifySimplePlaylist
import com.clipfinder.spotify.api.endpoint.PlaylistsEndpoints
import com.example.core.SpotifyDefaults
import com.example.core.model.Paged
import com.example.core.model.Resource
import com.example.core.retrofit.mapToResource
import com.example.spotify.account.playlist.domain.repo.ISpotifyAccountPlaylistsRepo
import io.reactivex.Single

class SpotifyAccountPlaylistsRepo(
    private val playlistsEndpoints: PlaylistsEndpoints
) : ISpotifyAccountPlaylistsRepo {

    override fun getCurrentUsersPlaylists(
        offset: Int
    ): Single<Resource<Paged<List<ISpotifySimplePlaylist>>>> = playlistsEndpoints
        .getAListOfCurrentUsersPlaylists(offset = offset)
        .mapToResource {
            Paged<List<ISpotifySimplePlaylist>>(
                contents = items,
                offset = offset + SpotifyDefaults.LIMIT,
                total = total
            )
        }
}
