package com.example.spotify.account.top.domain.repo

import com.clipfinder.core.spotify.model.ISpotifyArtist
import com.clipfinder.core.spotify.model.ISpotifyTrack
import com.example.core.model.Paged
import com.example.core.model.Resource
import io.reactivex.Single

interface ISpotifyAccountTopRepo {
    fun getCurrentUsersTopArtists(offset: Int): Single<Resource<Paged<List<ISpotifyArtist>>>>
    fun getCurrentUsersTopTracks(offset: Int): Single<Resource<Paged<List<ISpotifyTrack>>>>
}
