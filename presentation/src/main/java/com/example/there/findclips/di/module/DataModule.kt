package com.example.there.findclips.di.module

import android.arch.persistence.room.Room
import android.content.Context
import com.example.there.data.db.*
import com.example.there.data.repo.spotify.SpotifyRepository
import com.example.there.data.repo.spotify.datastore.SpotifyDbDataStore
import com.example.there.data.repo.spotify.datastore.SpotifyRemoteDataStore
import com.example.there.data.repo.videos.VideosRepository
import com.example.there.data.repo.videos.datastore.VideosDbDataStore
import com.example.there.data.repo.videos.datastore.VideosRemoteDataStore
import com.example.there.domain.repo.spotify.ISpotifyRepository
import com.example.there.domain.repo.spotify.datastore.ISpotifyDbDataStore
import com.example.there.domain.repo.spotify.datastore.ISpotifyRemoteDataStore
import com.example.there.domain.repo.videos.IVideosRepository
import com.example.there.domain.repo.videos.datastore.IVideosDbDataStore
import com.example.there.domain.repo.videos.datastore.IVideosRemoteDataStore
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
abstract class DataModule {

    @Module
    companion object {

        @Provides
        @Singleton
        @JvmStatic
        fun database(
                context: Context
        ): FindClipsDb = Room.databaseBuilder(context, FindClipsDb::class.java, "FindClipsDb.db")
                .build()

        @Provides
        @Singleton
        @JvmStatic
        fun albumDao(database: FindClipsDb): AlbumDao = database.albumDao()

        @Provides
        @Singleton
        @JvmStatic
        fun artistDao(database: FindClipsDb): ArtistDao = database.artistDao()

        @Provides
        @Singleton
        @JvmStatic
        fun categoryDao(database: FindClipsDb): CategoryDao = database.categoryDao()

        @Provides
        @Singleton
        @JvmStatic
        fun spotifyPlaylistDao(database: FindClipsDb): SpotifyPlaylistDao = database.spotifyPlaylistDao()

        @Provides
        @Singleton
        @JvmStatic
        fun trackDao(database: FindClipsDb): TrackDao = database.trackDao()

        @Provides
        @Singleton
        @JvmStatic
        fun videoDao(database: FindClipsDb): VideoDao = database.videoDao()

        @Provides
        @Singleton
        @JvmStatic
        fun videoPlaylistDao(database: FindClipsDb): VideoPlaylistDao = database.videoPlaylistDao()

        @Provides
        @Singleton
        @JvmStatic
        fun videoSearchDao(database: FindClipsDb): VideoSearchDao = database.videoSearchDao()

        @Provides
        @Singleton
        @JvmStatic
        fun relatedVideoSearchDao(database: FindClipsDb): RelatedVideoSearchDao = database.relatedVideoSearchDao()
    }

    @Binds
    abstract fun spotifyRemoteDataStore(ds: SpotifyRemoteDataStore): ISpotifyRemoteDataStore

    @Binds
    abstract fun spotifyDbDataStore(ds: SpotifyDbDataStore): ISpotifyDbDataStore

    @Binds
    abstract fun spotifyRepository(repo: SpotifyRepository): ISpotifyRepository

    @Binds
    abstract fun videosRemoteDataStore(ds: VideosRemoteDataStore): IVideosRemoteDataStore

    @Binds
    abstract fun videosDbDataStore(ds: VideosDbDataStore): IVideosDbDataStore

    @Binds
    abstract fun videosRepository(repo: VideosRepository): IVideosRepository
}