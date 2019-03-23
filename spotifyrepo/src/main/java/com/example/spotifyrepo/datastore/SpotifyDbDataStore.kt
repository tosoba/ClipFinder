package com.example.spotifyrepo.datastore

import com.example.core.util.ext.mapToSingleBoolean
import com.example.db.*
import com.example.db.model.spotify.*
import com.example.spotifyrepo.mapper.db
import com.example.spotifyrepo.mapper.domain
import com.example.there.domain.entity.spotify.*
import com.example.there.domain.repo.spotify.datastore.ISpotifyDbDataStore
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import javax.inject.Inject

class SpotifyDbDataStore @Inject constructor(
        private val albumDao: AlbumDao,
        private val artistDao: ArtistDao,
        private val categoryDao: CategoryDao,
        private val spotifyPlaylistDao: SpotifyPlaylistDao,
        private val trackDao: TrackDao
) : ISpotifyDbDataStore {

    override val favouriteAlbums: Flowable<List<AlbumEntity>>
        get() = albumDao.findAll().map { it.map(AlbumDbModel::domain) }

    override val favouriteArtists: Flowable<List<ArtistEntity>>
        get() = artistDao.findAll().map { it.map(ArtistDbModel::domain) }

    override val favouriteCategories: Flowable<List<CategoryEntity>>
        get() = categoryDao.findAll().map { it.map(CategoryDbModel::domain) }

    override val favouritePlaylists: Flowable<List<PlaylistEntity>>
        get() = spotifyPlaylistDao.findAll().map { it.map(PlaylistDbModel::domain) }

    override val favouriteTracks: Flowable<List<TrackEntity>>
        get() = trackDao.findAll().map { it.map(TrackDbModel::domain) }

    override fun insertAlbum(albumEntity: AlbumEntity): Completable = Completable.fromCallable {
        albumDao.insert(albumEntity.db)
    }

    override fun insertArtist(artistEntity: ArtistEntity): Completable = Completable.fromCallable {
        artistDao.insert(artistEntity.db)
    }

    override fun insertCategory(categoryEntity: CategoryEntity): Completable = Completable.fromCallable {
        categoryDao.insert(categoryEntity.db)
    }

    override fun insertPlaylist(playlistEntity: PlaylistEntity): Completable = Completable.fromCallable {
        spotifyPlaylistDao.insert(playlistEntity.db)
    }

    override fun insertTrack(trackEntity: TrackEntity): Completable = Completable.fromCallable {
        trackDao.insert(trackEntity.db)
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
        albumDao.delete(albumEntity.db)
    }

    override fun deleteArtist(artistEntity: ArtistEntity): Completable = Completable.fromCallable {
        artistDao.delete(artistEntity.db)
    }

    override fun deleteCategory(categoryEntity: CategoryEntity): Completable = Completable.fromCallable {
        categoryDao.delete(categoryEntity.db)
    }

    override fun deletePlaylist(playlistEntity: PlaylistEntity): Completable = Completable.fromCallable {
        spotifyPlaylistDao.delete(playlistEntity.db)
    }

    override fun deleteTrack(trackEntity: TrackEntity): Completable = Completable.fromCallable {
        trackDao.delete(trackEntity.db)
    }
}
