package com.example.spotify.account.top.domain.repo

import com.example.core.model.Paged
import com.example.core.model.Resource
import com.example.there.domain.entity.spotify.ArtistEntity
import com.example.there.domain.entity.spotify.TrackEntity
import io.reactivex.Single

interface ISpotifyAccountTopRepo {
    fun getCurrentUsersTopArtists(offset: Int): Single<Resource<Paged<List<ArtistEntity>>>>
    fun getCurrentUsersTopTracks(offset: Int): Single<Resource<Paged<List<TrackEntity>>>>
}
