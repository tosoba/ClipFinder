package com.example.there.data.repo.spotify.datastore

import com.example.there.data.db.*
import com.example.there.data.entity.spotify.*
import com.example.there.data.mapper.spotify.data
import com.example.there.data.mapper.spotify.domain
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

    override val favouriteAlbums: Flowable<List<AlbumEntity>>
        get() = albumDao.findAll().map { it.map(AlbumData::domain) }

    override val favouriteArtists: Flowable<List<ArtistEntity>>
        get() = artistDao.findAll().map { it.map(ArtistData::domain) }

    override val favouriteCategories: Flowable<List<CategoryEntity>>
        get() = categoryDao.findAll().map { it.map(CategoryData::domain) }

    override val favouritePlaylists: Flowable<List<PlaylistEntity>>
        get() = spotifyPlaylistDao.findAll().map { it.map(PlaylistData::domain) }

    override val favouriteTracks: Flowable<List<TrackEntity>>
        get() = trackDao.findAll().map { it.map(TrackData::domain) }

    override fun insertAlbum(albumEntity: AlbumEntity): Completable = Completable.fromCallable {
        albumDao.insert(albumEntity.data)
    }

    override fun insertArtist(artistEntity: ArtistEntity): Completable = Completable.fromCallable {
        artistDao.insert(artistEntity.data)
    }

    override fun insertCategory(categoryEntity: CategoryEntity): Completable = Completable.fromCallable {
        categoryDao.insert(categoryEntity.data)
    }

    override fun insertPlaylist(playlistEntity: PlaylistEntity): Completable = Completable.fromCallable {
        spotifyPlaylistDao.insert(playlistEntity.data)
    }

    override fun insertTrack(trackEntity: TrackEntity): Completable = Completable.fromCallable {
        trackDao.insert(trackEntity.data)
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
        albumDao.delete(albumEntity.data)
    }

    override fun deleteArtist(artistEntity: ArtistEntity): Completable = Completable.fromCallable {
        artistDao.delete(artistEntity.data)
    }

    override fun deleteCategory(categoryEntity: CategoryEntity): Completable = Completable.fromCallable {
        categoryDao.delete(categoryEntity.data)
    }

    override fun deletePlaylist(playlistEntity: PlaylistEntity): Completable = Completable.fromCallable {
        spotifyPlaylistDao.delete(playlistEntity.data)
    }

    override fun deleteTrack(trackEntity: TrackEntity): Completable = Completable.fromCallable {
        trackDao.delete(trackEntity.data)
    }
}
