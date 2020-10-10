package com.clipfinder.core.spotify.repo

import com.clipfinder.core.spotify.model.ISpotifyArtist
import com.clipfinder.core.spotify.model.ISpotifySimplifiedAlbum
import com.clipfinder.core.spotify.model.ISpotifyTrack
import com.example.core.model.Paged
import com.example.core.model.Resource
import io.reactivex.Single

interface ISpotifyRepo {
    fun getAlbum(id: String): Single<Resource<ISpotifySimplifiedAlbum>>
    fun getArtists(ids: List<String>): Single<Resource<List<ISpotifyArtist>>>
    fun getSimilarTracks(id: String, offset: Int): Single<Resource<Paged<List<ISpotifyTrack>>>>
}