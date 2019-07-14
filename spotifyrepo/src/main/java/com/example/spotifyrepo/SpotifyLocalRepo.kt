package com.example.spotifyrepo

import com.example.core.ext.isPresent
import com.example.db.*
import com.example.db.model.spotify.*
import com.example.spotifyrepo.mapper.db
import com.example.spotifyrepo.mapper.domain
import com.example.there.domain.entity.spotify.*
import com.example.there.domain.repo.spotify.ISpotifyLocalRepo
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import javax.inject.Inject

class SpotifyLocalRepo @Inject constructor(
        private val albumDao: AlbumDao,
        private val artistDao: ArtistDao,
        private val categoryDao: CategoryDao,
        private val spotifyPlaylistDao: SpotifyPlaylistDao,
        private val trackDao: TrackDao
) : ISpotifyLocalRepo {

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

    override fun insertAlbum(album: AlbumEntity): Completable = Completable.fromCallable {
        albumDao.insert(album.db)
    }

    override fun insertArtist(artist: ArtistEntity): Completable = Completable.fromCallable {
        artistDao.insert(artist.db)
    }

    override fun insertCategory(category: CategoryEntity): Completable = Completable.fromCallable {
        categoryDao.insert(category.db)
    }

    override fun insertPlaylist(playlist: PlaylistEntity): Completable = Completable.fromCallable {
        spotifyPlaylistDao.insert(playlist.db)
    }

    override fun insertTrack(track: TrackEntity): Completable = Completable.fromCallable {
        trackDao.insert(track.db)
    }

    override fun isAlbumSaved(
            album: AlbumEntity
    ): Single<Boolean> = albumDao.findById(album.id).isPresent()

    override fun isArtistSaved(
            artist: ArtistEntity
    ): Single<Boolean> = artistDao.findById(artist.id).isPresent()

    override fun isCategorySaved(
            category: CategoryEntity
    ): Single<Boolean> = artistDao.findById(category.id).isPresent()

    override fun isPlaylistSaved(
            playlist: PlaylistEntity
    ): Single<Boolean> = spotifyPlaylistDao.findById(playlist.id).isPresent()

    override fun isTrackSaved(
            track: TrackEntity
    ): Single<Boolean> = trackDao.findById(track.id).isPresent()

    override fun deleteAlbum(album: AlbumEntity): Completable = Completable.fromCallable {
        albumDao.delete(album.db)
    }

    override fun deleteArtist(artist: ArtistEntity): Completable = Completable.fromCallable {
        artistDao.delete(artist.db)
    }

    override fun deleteCategory(category: CategoryEntity): Completable = Completable.fromCallable {
        categoryDao.delete(category.db)
    }

    override fun deletePlaylist(playlist: PlaylistEntity): Completable = Completable.fromCallable {
        spotifyPlaylistDao.delete(playlist.db)
    }

    override fun deleteTrack(track: TrackEntity): Completable = Completable.fromCallable {
        trackDao.delete(track.db)
    }
}
