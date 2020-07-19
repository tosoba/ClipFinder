package com.example.spotify.album.domain.repo

import com.example.core.model.Paged
import com.example.core.model.Resource
import com.example.there.domain.entity.spotify.TrackEntity
import io.reactivex.Single

interface ISpotifyAlbumRepo {
    fun getTracksFromAlbum(albumId: String, offset: Int): Single<Resource<Paged<List<TrackEntity>>>>
}