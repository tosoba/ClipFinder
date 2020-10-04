package com.example.spotify.album.domain.repo

import com.clipfinder.core.spotify.model.ISpotifyTrack
import com.example.core.model.Paged
import com.example.core.model.Resource
import io.reactivex.Single

interface ISpotifyAlbumRepo {
    fun getTracksFromAlbum(albumId: String, offset: Int): Single<Resource<Paged<List<ISpotifyTrack>>>>
}