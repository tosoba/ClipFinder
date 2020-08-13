package com.example.spotify.account.playlist.domain.repo

import com.example.core.model.Paged
import com.example.core.model.Resource
import com.example.there.domain.entity.spotify.PlaylistEntity
import io.reactivex.Single

interface ISpotifyAccountPlaylistsRepo {
    fun getCurrentUsersPlaylists(offset: Int): Single<Resource<Paged<List<PlaylistEntity>>>>
}
