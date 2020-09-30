package com.example.spotify.account.playlist.domain.repo

import com.clipfinder.core.spotify.model.ISpotifySimplePlaylist
import com.example.core.model.Paged
import com.example.core.model.Resource
import io.reactivex.Single

interface ISpotifyAccountPlaylistsRepo {
    fun getCurrentUsersPlaylists(offset: Int): Single<Resource<Paged<List<ISpotifySimplePlaylist>>>>
}
