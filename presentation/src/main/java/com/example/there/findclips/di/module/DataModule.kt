package com.example.there.findclips.di.module

import android.arch.persistence.room.Room
import android.content.Context
import com.example.there.domain.repo.soundcloud.ISoundCloudRepository
import com.example.there.domain.repo.soundcloud.datastore.ISoundCloudDbDataStore
import com.example.there.domain.repo.soundcloud.datastore.ISoundCloudRemoteDataStore
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
        ): com.example.db.model.FindClipsDb = Room.databaseBuilder(context, com.example.db.model.FindClipsDb::class.java, "FindClipsDb.db")
                .build()

        @Provides
        @Singleton
        @JvmStatic
        fun albumDao(database: com.example.db.model.FindClipsDb): com.example.db.model.AlbumDao = database.albumDao()

        @Provides
        @Singleton
        @JvmStatic
        fun artistDao(database: com.example.db.model.FindClipsDb): com.example.db.model.ArtistDao = database.artistDao()

        @Provides
        @Singleton
        @JvmStatic
        fun categoryDao(database: com.example.db.model.FindClipsDb): com.example.db.model.CategoryDao = database.categoryDao()

        @Provides
        @Singleton
        @JvmStatic
        fun spotifyPlaylistDao(database: com.example.db.model.FindClipsDb): com.example.db.model.SpotifyPlaylistDao = database.spotifyPlaylistDao()

        @Provides
        @Singleton
        @JvmStatic
        fun trackDao(database: com.example.db.model.FindClipsDb): com.example.db.model.TrackDao = database.trackDao()

        @Provides
        @Singleton
        @JvmStatic
        fun videoDao(database: com.example.db.model.FindClipsDb): com.example.db.model.VideoDao = database.videoDao()

        @Provides
        @Singleton
        @JvmStatic
        fun videoPlaylistDao(database: com.example.db.model.FindClipsDb): com.example.db.model.VideoPlaylistDao = database.videoPlaylistDao()

        @Provides
        @Singleton
        @JvmStatic
        fun videoSearchDao(database: com.example.db.model.FindClipsDb): com.example.db.model.VideoSearchDao = database.videoSearchDao()

        @Provides
        @Singleton
        @JvmStatic
        fun relatedVideoSearchDao(database: com.example.db.model.FindClipsDb): com.example.db.model.RelatedVideoSearchDao = database.relatedVideoSearchDao()
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

    @Binds
    abstract fun soundCloudDbDataStore(ds: SoundCloudDbDataStore): ISoundCloudDbDataStore

    @Binds
    abstract fun soundCloudRemoteDataStore(ds: SoundCloudRemoteDataStore): ISoundCloudRemoteDataStore

    @Binds
    abstract fun soundCloudRepository(repo: SoundCloudRepository): ISoundCloudRepository
}