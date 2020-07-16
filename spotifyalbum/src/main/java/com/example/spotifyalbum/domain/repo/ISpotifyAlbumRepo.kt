package com.example.spotifyalbum.domain.repo

import com.example.core.model.Resource
import com.example.there.domain.entity.ListPage
import com.example.there.domain.entity.spotify.TrackEntity
import io.reactivex.Single

interface ISpotifyAlbumRepo {
    fun getTracksFromAlbum(albumId: String, offset: Int): Single<Resource<ListPage<TrackEntity>>>
}