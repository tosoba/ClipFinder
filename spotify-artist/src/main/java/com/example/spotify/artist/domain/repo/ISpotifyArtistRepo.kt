package com.example.spotify.artist.domain.repo

import com.clipfinder.core.spotify.model.ISpotifyArtist
import com.clipfinder.core.spotify.model.ISpotifySimplifiedAlbum
import com.clipfinder.core.spotify.model.ISpotifyTrack
import com.example.core.model.Paged
import com.example.core.model.Resource
import io.reactivex.Single

interface ISpotifyArtistRepo {
    fun getAlbumsFromArtist(artistId: String, offset: Int): Single<Resource<Paged<List<ISpotifySimplifiedAlbum>>>>
    fun getRelatedArtists(artistId: String): Single<Resource<List<ISpotifyArtist>>>
    fun getTopTracksFromArtist(artistId: String): Single<Resource<List<ISpotifyTrack>>>
}
