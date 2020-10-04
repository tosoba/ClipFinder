package com.example.spotify.account.saved.domain.repo

import com.clipfinder.core.spotify.model.ISpotifySimplifiedAlbum
import com.clipfinder.core.spotify.model.ISpotifyTrack
import com.example.core.model.Paged
import com.example.core.model.Resource
import io.reactivex.Single

interface ISpotifyAccountSavedRepo {
    fun getCurrentUsersSavedTracks(offset: Int): Single<Resource<Paged<List<ISpotifyTrack>>>>
    fun getCurrentUsersSavedAlbums(offset: Int): Single<Resource<Paged<List<ISpotifySimplifiedAlbum>>>>
}
