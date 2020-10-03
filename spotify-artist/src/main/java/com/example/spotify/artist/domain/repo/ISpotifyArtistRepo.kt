package com.example.spotify.artist.domain.repo

import com.example.core.model.Paged
import com.example.core.model.Resource
import com.example.there.domain.entity.spotify.AlbumEntity
import com.example.there.domain.entity.spotify.ArtistEntity
import com.example.there.domain.entity.spotify.TrackEntity
import io.reactivex.Completable
import io.reactivex.Single

interface ISpotifyArtistRepo {
    fun getAlbumsFromArtist(artistId: String, offset: Int): Single<Resource<Paged<List<AlbumEntity>>>>
    fun getRelatedArtists(artistId: String): Single<Resource<List<ArtistEntity>>>
    fun getTopTracksFromArtist(artistId: String): Single<Resource<List<TrackEntity>>>
}
