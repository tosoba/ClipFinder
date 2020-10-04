package com.example.spotify.playlist.domain.repo

import com.clipfinder.core.spotify.model.ISpotifyTrack
import com.example.core.model.Paged
import com.example.core.model.Resource
import io.reactivex.Single

interface ISpotifyPlaylistRepo {
    fun getPlaylistTracks(playlistId: String, offset: Int): Single<Resource<Paged<List<ISpotifyTrack>>>>
}