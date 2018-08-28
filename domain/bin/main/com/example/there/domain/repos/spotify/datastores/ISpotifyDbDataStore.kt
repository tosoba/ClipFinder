package com.example.there.domain.repos.spotify.datastores

import com.example.there.domain.entities.spotify.*
import io.reactivex.Completable
import io.reactivex.Single

interface ISpotifyDbDataStore {
    fun getFavouriteAlbums(): Single<List<AlbumEntity>>
    fun getFavouriteArtists(): Single<List<ArtistEntity>>
    fun getFavouriteCategories(): Single<List<CategoryEntity>>
    fun getFavouritePlaylists(): Single<List<PlaylistEntity>>
    fun getFavouriteTracks(): Single<List<TrackEntity>>

    fun insertAlbum(albumEntity: AlbumEntity): Completable
    fun insertArtist(artistEntity: ArtistEntity): Completable
    fun insertCategory(categoryEntity: CategoryEntity): Completable
    fun insertPlaylist(playlistEntity: PlaylistEntity): Completable
    fun insertTrack(trackEntity: TrackEntity): Completable
}