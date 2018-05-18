package com.example.there.findclips.di.modules

import android.app.Application
import android.arch.persistence.room.Room
import com.example.there.data.apis.spotify.SpotifyAccountsApi
import com.example.there.data.apis.spotify.SpotifyApi
import com.example.there.data.apis.spotify.SpotifyChartsApi
import com.example.there.data.apis.youtube.YoutubeApi
import com.example.there.data.db.*
import com.example.there.data.repos.spotify.SpotifyRepository
import com.example.there.data.repos.spotify.datastores.SpotifyDbDataStore
import com.example.there.data.repos.spotify.datastores.SpotifyRemoteDataStore
import com.example.there.data.repos.videos.VideosRepository
import com.example.there.data.repos.videos.datastores.VideosDbDataStore
import com.example.there.data.repos.videos.datastores.VideosRemoteDataStore
import com.example.there.domain.repos.spotify.ISpotifyRepository
import com.example.there.domain.repos.spotify.datastores.ISpotifyDbDataStore
import com.example.there.domain.repos.spotify.datastores.ISpotifyRemoteDataStore
import com.example.there.domain.repos.videos.IVideosRepository
import com.example.there.domain.repos.videos.datastores.IVideosDbDataStore
import com.example.there.domain.repos.videos.datastores.IVideosRemoteDataStore
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DataModule(app: Application) {

    private val database: FindClipsDb = Room.databaseBuilder(app.applicationContext, FindClipsDb::class.java, "FindClipsDb.db").build()

    @Provides
    @Singleton
    fun database(): FindClipsDb = database

    @Provides
    @Singleton
    fun albumDao(): AlbumDao = database.albumDao()

    @Provides
    @Singleton
    fun artistDao(): ArtistDao = database.artistDao()

    @Provides
    @Singleton
    fun categoryDao(): CategoryDao = database.categoryDao()

    @Provides
    fun spotifyPlaylistDao(): SpotifyPlaylistDao = database.spotifyPlaylistDao()

    @Provides
    @Singleton
    fun trackDao(): TrackDao = database.trackDao()

    @Provides
    @Singleton
    fun videoDao(): VideoDao = database.videoDao()

    @Provides
    @Singleton
    fun videoPlaylistDao(): VideoPlaylistDao = database.videoPlaylistDao()

    @Provides
    @Singleton
    fun spotifyRemoteDataStore(api: SpotifyApi,
                               accountsApi: SpotifyAccountsApi,
                               chartsApi: SpotifyChartsApi): ISpotifyRemoteDataStore = SpotifyRemoteDataStore(api, accountsApi, chartsApi)

    @Provides
    @Singleton
    fun spotifyDbDataStore(albumDao: AlbumDao,
                           artistDao: ArtistDao,
                           categoryDao: CategoryDao,
                           spotifyPlaylistDao: SpotifyPlaylistDao,
                           trackDao: TrackDao): ISpotifyDbDataStore =
            SpotifyDbDataStore(albumDao, artistDao, categoryDao, spotifyPlaylistDao, trackDao)

    @Provides
    @Singleton
    fun spotifyRepository(remoteDataStore: ISpotifyRemoteDataStore,
                          dbDataStore: ISpotifyDbDataStore): ISpotifyRepository = SpotifyRepository(remoteDataStore, dbDataStore)

    @Provides
    @Singleton
    fun videosRemoteDataStore(api: YoutubeApi): IVideosRemoteDataStore = VideosRemoteDataStore(api)

    @Provides
    @Singleton
    fun videosDbDataStore(videoDao: VideoDao,
                          videoPlaylistDao: VideoPlaylistDao): IVideosDbDataStore = VideosDbDataStore(videoDao, videoPlaylistDao)

    @Provides
    @Singleton
    fun videosRepository(remoteDataStore: IVideosRemoteDataStore,
                         dbDataStore: IVideosDbDataStore): IVideosRepository = VideosRepository(remoteDataStore, dbDataStore)
}