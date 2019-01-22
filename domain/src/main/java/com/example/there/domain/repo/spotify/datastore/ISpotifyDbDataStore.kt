package com.example.there.domain.repo.spotify.datastore

import com.example.there.domain.entity.spotify.*
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

interface ISpotifyDbDataStore {
    val favouriteAlbums: Flowable<List<AlbumEntity>>
    val favouriteArtists: Flowable<List<ArtistEntity>>
    val favouriteCategories: Flowable<List<CategoryEntity>>
    val favouritePlaylists: Flowable<List<PlaylistEntity>>
    val favouriteTracks: Flowable<List<TrackEntity>>

    fun insertAlbum(albumEntity: AlbumEntity): Completable
    fun insertArtist(artistEntity: ArtistEntity): Completable
    fun insertCategory(categoryEntity: CategoryEntity): Completable
    fun insertPlaylist(playlistEntity: PlaylistEntity): Completable
    fun insertTrack(trackEntity: TrackEntity): Completable

    fun isAlbumSaved(albumEntity: AlbumEntity): Single<Boolean>
    fun isArtistSaved(artistEntity: ArtistEntity): Single<Boolean>
    fun isCategorySaved(categoryEntity: CategoryEntity): Single<Boolean>
    fun isPlaylistSaved(playlistEntity: PlaylistEntity): Single<Boolean>
    fun isTrackSaved(trackEntity: TrackEntity): Single<Boolean>

    fun deleteAlbum(albumEntity: AlbumEntity): Completable
    fun deleteArtist(artistEntity: ArtistEntity): Completable
    fun deleteCategory(categoryEntity: CategoryEntity): Completable
    fun deletePlaylist(playlistEntity: PlaylistEntity): Completable
    fun deleteTrack(trackEntity: TrackEntity): Completable
}