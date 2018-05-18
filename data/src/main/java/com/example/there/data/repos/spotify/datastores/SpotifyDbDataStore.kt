package com.example.there.data.repos.spotify.datastores

import com.example.there.data.db.*
import com.example.there.data.mappers.spotify.*
import com.example.there.domain.entities.spotify.*
import com.example.there.domain.repos.spotify.datastores.ISpotifyDbDataStore
import io.reactivex.Single

class SpotifyDbDataStore(private val albumDao: AlbumDao,
                         private val artistDao: ArtistDao,
                         private val categoryDao: CategoryDao,
                         private val spotifyPlaylistDao: SpotifyPlaylistDao,
                         private val trackDao: TrackDao) : ISpotifyDbDataStore {

    override fun getFavouriteAlbums(): Single<List<AlbumEntity>> = albumDao.findAll().map { it.map(AlbumMapper::mapFrom) }

    override fun getFavouriteArtists(): Single<List<ArtistEntity>> = artistDao.findAll().map { it.map(ArtistMapper::mapFrom) }

    override fun getFavouriteCategories(): Single<List<CategoryEntity>> = categoryDao.findAll().map { it.map(CategoryMapper::mapFrom) }

    override fun getFavouritePlaylists(): Single<List<PlaylistEntity>> = spotifyPlaylistDao.findAll().map { it.map(PlaylistMapper::mapFrom) }

    override fun getFavouriteTracks(): Single<List<TrackEntity>> = trackDao.findAll().map { it.map(TrackMapper::mapFrom) }
}