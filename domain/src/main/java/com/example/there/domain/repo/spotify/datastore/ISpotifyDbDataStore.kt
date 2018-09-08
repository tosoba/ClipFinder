package com.example.there.domain.repo.spotify.datastore

import com.example.there.domain.entity.spotify.*
import io.reactivex.Completable
import io.reactivex.Flowable

interface ISpotifyDbDataStore {
    fun getFavouriteAlbums(): Flowable<List<AlbumEntity>>
    fun getFavouriteArtists(): Flowable<List<ArtistEntity>>
    fun getFavouriteCategories(): Flowable<List<CategoryEntity>>
    fun getFavouritePlaylists(): Flowable<List<PlaylistEntity>>
    fun getFavouriteTracks(): Flowable<List<TrackEntity>>

    fun insertAlbum(albumEntity: AlbumEntity): Completable
    fun insertArtist(artistEntity: ArtistEntity): Completable
    fun insertCategory(categoryEntity: CategoryEntity): Completable
    fun insertPlaylist(playlistEntity: PlaylistEntity): Completable
    fun insertTrack(trackEntity: TrackEntity): Completable
}