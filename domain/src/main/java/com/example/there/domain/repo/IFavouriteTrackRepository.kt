package com.example.there.domain.repo

import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

interface IFavouriteTrackRepository<Track> {
    val favouriteTracks: Flowable<List<Track>>
    fun isTrackSaved(track: Track): Single<Boolean>
    fun insertTrack(track: Track): Completable
    fun deleteTrack(track: Track): Completable
}

interface IFavouritePlaylistRepository<Playlist> {
    val favouritePlaylists: Flowable<List<Playlist>>
    fun isPlaylistSaved(playlist: Playlist): Single<Boolean>
    fun insertPlaylist(playlist: Playlist): Completable
    fun deletePlaylist(playlist: Playlist): Completable
}

interface IFavouriteAlbumRepository<Album> {
    val favouriteAlbums: Flowable<List<Album>>
    fun isAlbumSaved(album: Album): Single<Boolean>
    fun insertAlbum(album: Album): Completable
    fun deleteAlbum(album: Album): Completable
}

interface IFavouriteArtistRepository<Artist> {
    val favouriteArtists: Flowable<List<Artist>>
    fun isArtistSaved(artist: Artist): Single<Boolean>
    fun insertArtist(artist: Artist): Completable
    fun deleteArtist(artist: Artist): Completable
}

interface IFavouriteCategoryRepository<Category> {
    val favouriteCategories: Flowable<List<Category>>
    fun isCategorySaved(category: Category): Single<Boolean>
    fun insertCategory(category: Category): Completable
    fun deleteCategory(category: Category): Completable
}
