package com.example.spotifyartist.domain.repo

import com.example.core.model.Paged
import com.example.core.model.Resource
import com.example.there.domain.entity.spotify.AlbumEntity
import com.example.there.domain.entity.spotify.ArtistEntity
import com.example.there.domain.entity.spotify.TrackEntity
import io.reactivex.Completable
import io.reactivex.Single

interface ISpotifyArtistRepo {
    fun deleteArtist(artist: ArtistEntity): Completable
    fun getAlbumsFromArtist(artistId: String, offset: Int): Single<Resource<Paged<List<AlbumEntity>>>>
    fun getRelatedArtists(artistId: String): Single<Resource<List<ArtistEntity>>>
    fun getTopTracksFromArtist(artistId: String): Single<Resource<List<TrackEntity>>>
    fun insertArtist(artist: ArtistEntity): Completable
    fun isArtistSaved(artistId: String): Single<Boolean>
}
