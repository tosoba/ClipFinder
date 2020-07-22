package com.example.spotify.playlist.domain.repo

import com.example.core.model.Paged
import com.example.core.model.Resource
import com.example.there.domain.entity.spotify.TrackEntity
import io.reactivex.Single

interface ISpotifyPlaylistRepo {
    fun getPlaylistTracks(playlistId: String, userId: String, offset: Int): Single<Resource<Paged<List<TrackEntity>>>>
}