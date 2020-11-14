package com.example.spotify.account.playlist.data

import com.clipfinder.core.spotify.model.ISpotifySimplifiedPlaylist
import com.clipfinder.spotify.api.endpoint.PlaylistsEndpoints
import com.example.core.SpotifyDefaults
import com.example.core.model.Paged
import com.example.core.model.Resource
import com.example.core.ext.mapToResource
import com.example.core.ext.toPaged
import com.example.spotify.account.playlist.domain.repo.ISpotifyAccountPlaylistsRepo
import io.reactivex.Single

class SpotifyAccountPlaylistsRepo(
    private val playlistsEndpoints: PlaylistsEndpoints
) : ISpotifyAccountPlaylistsRepo {

    override fun getCurrentUsersPlaylists(
        offset: Int
    ): Single<Resource<Paged<List<ISpotifySimplifiedPlaylist>>>> = playlistsEndpoints
        .getAListOfCurrentUsersPlaylists(offset = offset)
        .mapToResource { toPaged() }
}
