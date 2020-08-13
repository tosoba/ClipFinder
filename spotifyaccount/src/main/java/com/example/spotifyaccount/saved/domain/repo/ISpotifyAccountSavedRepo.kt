package com.example.spotifyaccount.saved.domain.repo

import com.example.core.model.Paged
import com.example.core.model.Resource
import com.example.there.domain.entity.spotify.AlbumEntity
import com.example.there.domain.entity.spotify.TrackEntity
import io.reactivex.Single

interface ISpotifyAccountSavedRepo {
    fun getCurrentUsersSavedTracks(offset: Int): Single<Resource<Paged<List<TrackEntity>>>>
    fun getCurrentUsersSavedAlbums(offset: Int): Single<Resource<Paged<List<AlbumEntity>>>>
}
