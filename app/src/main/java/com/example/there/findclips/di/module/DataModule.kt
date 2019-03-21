package com.example.there.findclips.di.module

import android.arch.persistence.room.Room
import android.content.Context
import com.example.db.*
import com.example.soundcloudrepo.SoundCloudRepository
import com.example.soundcloudrepo.datastore.SoundCloudDbDataStore
import com.example.soundcloudrepo.datastore.SoundCloudRemoteDataStore
import com.example.spotifyrepo.SpotifyRepository
import com.example.spotifyrepo.datastore.SpotifyDbDataStore
import com.example.spotifyrepo.datastore.SpotifyRemoteDataStore
import com.example.there.domain.repo.soundcloud.ISoundCloudRepository
import com.example.there.domain.repo.soundcloud.datastore.ISoundCloudDbDataStore
import com.example.there.domain.repo.soundcloud.datastore.ISoundCloudRemoteDataStore
import com.example.there.domain.repo.spotify.ISpotifyRepository
import com.example.there.domain.repo.spotify.datastore.ISpotifyDbDataStore
import com.example.there.domain.repo.spotify.datastore.ISpotifyRemoteDataStore
import com.example.there.domain.repo.videos.IVideosRepository
import com.example.there.domain.repo.videos.datastore.IVideosDbDataStore
import com.example.there.domain.repo.videos.datastore.IVideosRemoteDataStore
import com.example.videosrepo.VideosRepository
import com.example.videosrepo.datastore.VideosDbDataStore
import com.example.videosrepo.datastore.VideosRemoteDataStore
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
        @JvmStatic
        fun albumDao(database: FindClipsDb): AlbumDao = database.albumDao()

        @Provides
        @JvmStatic
        fun artistDao(database: FindClipsDb): ArtistDao = database.artistDao()

        @Provides
        @JvmStatic
        fun categoryDao(database: FindClipsDb): CategoryDao = database.categoryDao()

        @Provides
        @JvmStatic
        fun spotifyPlaylistDao(database: FindClipsDb): SpotifyPlaylistDao = database.spotifyPlaylistDao()

        @Provides
        @JvmStatic
        fun trackDao(database: FindClipsDb): TrackDao = database.trackDao()

        @Provides
        @JvmStatic
        fun videoDao(database: FindClipsDb): VideoDao = database.videoDao()

        @Provides
        @JvmStatic
        fun videoPlaylistDao(database: FindClipsDb): VideoPlaylistDao = database.videoPlaylistDao()

        @Provides
        @JvmStatic
        fun videoSearchDao(database: FindClipsDb): VideoSearchDao = database.videoSearchDao()

        @Provides
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

    @Binds
    abstract fun soundCloudDbDataStore(ds: SoundCloudDbDataStore): ISoundCloudDbDataStore

    @Binds
    abstract fun soundCloudRemoteDataStore(ds: SoundCloudRemoteDataStore): ISoundCloudRemoteDataStore

    @Binds
    abstract fun soundCloudRepository(repo: SoundCloudRepository): ISoundCloudRepository
}