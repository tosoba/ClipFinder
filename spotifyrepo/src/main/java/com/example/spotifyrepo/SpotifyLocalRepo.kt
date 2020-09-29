package com.example.spotifyrepo

import com.example.core.ext.isPresent
import com.example.db.AlbumDao
import com.example.db.ArtistDao
import com.example.db.SpotifyPlaylistDao
import com.example.db.TrackDao
import com.example.db.model.spotify.AlbumDbModel
import com.example.db.model.spotify.ArtistDbModel
import com.example.db.model.spotify.PlaylistDbModel
import com.example.db.model.spotify.TrackDbModel
import com.example.spotifyrepo.mapper.db
import com.example.spotifyrepo.mapper.domain
import com.example.there.domain.entity.spotify.AlbumEntity
import com.example.there.domain.entity.spotify.ArtistEntity
import com.example.there.domain.entity.spotify.PlaylistEntity
import com.example.there.domain.entity.spotify.TrackEntity
import com.example.there.domain.repo.spotify.ISpotifyLocalRepo
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

class SpotifyLocalRepo(
    private val albumDao: AlbumDao,
    private val artistDao: ArtistDao,
    private val spotifyPlaylistDao: SpotifyPlaylistDao,
    private val trackDao: TrackDao
) : ISpotifyLocalRepo {

    override val favouriteAlbums: Flowable<List<AlbumEntity>>
        get() = albumDao.findAll().map { it.map(AlbumDbModel::domain) }

    override val favouriteArtists: Flowable<List<ArtistEntity>>
        get() = artistDao.findAll().map { it.map(ArtistDbModel::domain) }

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

    override fun insertPlaylist(playlist: PlaylistEntity): Completable = Completable.fromCallable {
        spotifyPlaylistDao.insert(playlist.db)
    }

    override fun insertTrack(track: TrackEntity): Completable = Completable.fromCallable {
        trackDao.insert(track.db)
    }

    override fun isAlbumSaved(
        albumId: String
    ): Single<Boolean> = albumDao.findById(albumId).isPresent()

    override fun isArtistSaved(
        artistId: String
    ): Single<Boolean> = artistDao.findById(artistId).isPresent()

    override fun isPlaylistSaved(
        playlistId: String
    ): Single<Boolean> = spotifyPlaylistDao.findById(playlistId).isPresent()

    override fun isTrackSaved(
        trackId: String
    ): Single<Boolean> = trackDao.findById(trackId).isPresent()

    override fun deleteAlbum(album: AlbumEntity): Completable = Completable.fromCallable {
        albumDao.delete(album.db)
    }

    override fun deleteArtist(artist: ArtistEntity): Completable = Completable.fromCallable {
        artistDao.delete(artist.db)
    }

    override fun deletePlaylist(playlist: PlaylistEntity): Completable = Completable.fromCallable {
        spotifyPlaylistDao.delete(playlist.db)
    }

    override fun deleteTrack(track: TrackEntity): Completable = Completable.fromCallable {
        trackDao.delete(track.db)
    }
}
