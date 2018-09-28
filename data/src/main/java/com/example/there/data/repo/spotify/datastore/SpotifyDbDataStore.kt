package com.example.there.data.repo.spotify.datastore

import com.example.there.data.db.*
import com.example.there.data.mapper.spotify.*
import com.example.there.data.util.mapToSingleBoolean
import com.example.there.domain.entity.spotify.*
import com.example.there.domain.repo.spotify.datastore.ISpotifyDbDataStore
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SpotifyDbDataStore @Inject constructor(
        private val albumDao: AlbumDao,
        private val artistDao: ArtistDao,
        private val categoryDao: CategoryDao,
        private val spotifyPlaylistDao: SpotifyPlaylistDao,
        private val trackDao: TrackDao
) : ISpotifyDbDataStore {

    override fun getFavouriteAlbums(): Flowable<List<AlbumEntity>> = albumDao.findAll()
            .map { it.map(AlbumMapper::mapFrom) }

    override fun getFavouriteArtists(): Flowable<List<ArtistEntity>> = artistDao.findAll()
            .map { it.map(ArtistMapper::mapFrom) }

    override fun getFavouriteCategories(): Flowable<List<CategoryEntity>> = categoryDao.findAll()
            .map { it.map(CategoryMapper::mapFrom) }

    override fun getFavouritePlaylists(): Flowable<List<PlaylistEntity>> = spotifyPlaylistDao.findAll()
            .map { it.map(PlaylistMapper::mapFrom) }

    override fun getFavouriteTracks(): Flowable<List<TrackEntity>> = trackDao.findAll()
            .map { it.map(TrackMapper::mapFrom) }

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

    override fun isAlbumSaved(
            albumEntity: AlbumEntity
    ): Single<Boolean> = albumDao.findById(albumEntity.id).mapToSingleBoolean()

    override fun isArtistSaved(
            artistEntity: ArtistEntity
    ): Single<Boolean> = artistDao.findById(artistEntity.id).mapToSingleBoolean()

    override fun isCategorySaved(
            categoryEntity: CategoryEntity
    ): Single<Boolean> = artistDao.findById(categoryEntity.id).mapToSingleBoolean()

    override fun isPlaylistSaved(
            playlistEntity: PlaylistEntity
    ): Single<Boolean> = spotifyPlaylistDao.findById(playlistEntity.id).mapToSingleBoolean()

    override fun isTrackSaved(
            trackEntity: TrackEntity
    ): Single<Boolean> = trackDao.findById(trackEntity.id).mapToSingleBoolean()

    override fun deleteAlbum(albumEntity: AlbumEntity): Completable = Completable.fromCallable {
        albumDao.delete(AlbumMapper.mapBack(albumEntity))
    }

    override fun deleteArtist(artistEntity: ArtistEntity): Completable = Completable.fromCallable {
        artistDao.delete(ArtistMapper.mapBack(artistEntity))
    }

    override fun deleteCategory(categoryEntity: CategoryEntity): Completable = Completable.fromCallable {
        categoryDao.delete(CategoryMapper.mapBack(categoryEntity))
    }

    override fun deletePlaylist(playlistEntity: PlaylistEntity): Completable = Completable.fromCallable {
        spotifyPlaylistDao.delete(PlaylistMapper.mapBack(playlistEntity))
    }

    override fun deleteTrack(trackEntity: TrackEntity): Completable = Completable.fromCallable {
        trackDao.delete(TrackMapper.mapBack(trackEntity))
    }
}
