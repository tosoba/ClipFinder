package com.example.there.data.repo.spotify.datastore

import com.example.there.data.db.*
import com.example.there.data.mapper.spotify.*
import com.example.there.domain.entity.spotify.*
import com.example.there.domain.repo.spotify.datastore.ISpotifyDbDataStore
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

class SpotifyDbDataStore @Inject constructor(
        private val albumDao: AlbumDao,
        private val artistDao: ArtistDao,
        private val categoryDao: CategoryDao,
        private val spotifyPlaylistDao: SpotifyPlaylistDao,
        private val trackDao: TrackDao
) : ISpotifyDbDataStore {

    override fun getFavouriteAlbums(): Single<List<AlbumEntity>> = albumDao.findAll().map { it.map(AlbumMapper::mapFrom) }

    override fun getFavouriteArtists(): Single<List<ArtistEntity>> = artistDao.findAll().map { it.map(ArtistMapper::mapFrom) }

    override fun getFavouriteCategories(): Single<List<CategoryEntity>> = categoryDao.findAll().map { it.map(CategoryMapper::mapFrom) }

    override fun getFavouritePlaylists(): Single<List<PlaylistEntity>> = spotifyPlaylistDao.findAll().map { it.map(PlaylistMapper::mapFrom) }

    override fun getFavouriteTracks(): Single<List<TrackEntity>> = trackDao.findAll().map { it.map(TrackMapper::mapFrom) }

    override fun insertAlbum(albumEntity: AlbumEntity): Completable = Completable.fromCallable {
        albumDao.insert(AlbumMapper.mapBack(albumEntity))
    }

    override fun insertArtist(artistEntity: ArtistEntity): Completable = Completable.fromCallable {
        artistDao.insert(ArtistMapper.mapBack(artistEntity))
    }

    override fun insertCategory(categoryEntity: CategoryEntity): Completable = Completable.fromCallable {
        categoryDao.insert(CategoryMapper.mapBack(categoryEntity))
    }

    override fun insertPlaylist(playlistEntity: PlaylistEntity): Completable = Completable.fromCallable {
        spotifyPlaylistDao.insert(PlaylistMapper.mapBack(playlistEntity))
    }

    override fun insertTrack(trackEntity: TrackEntity): Completable = Completable.fromCallable {
        trackDao.insert(TrackMapper.mapBack(trackEntity))
    }
}