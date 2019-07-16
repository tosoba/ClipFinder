package com.example.there.findclips.di.module

import android.content.Context
import androidx.room.Room
import com.example.db.*
import com.example.soundcloudrepo.SoundCloudDbDataStore
import com.example.soundcloudrepo.SoundCloudRemoteDataStore
import com.example.spotifyrepo.SpotifyLocalRepo
import com.example.spotifyrepo.SpotifyRemoteRepo
import com.example.there.domain.repo.soundcloud.ISoundCloudDbDataStore
import com.example.there.domain.repo.soundcloud.ISoundCloudRemoteDataStore
import com.example.there.domain.repo.spotify.ISpotifyLocalRepo
import com.example.there.domain.repo.spotify.ISpotifyRemoteDataStore
import com.example.there.domain.repo.videos.IVideosDbDataStore
import com.example.there.domain.repo.videos.IVideosRemoteDataStore
import com.example.videosrepo.VideosDbDataStore
import com.example.videosrepo.VideosRemoteDataStore
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
        fun database(context: Context): FindClipsDb = Room.databaseBuilder(
                context, FindClipsDb::class.java, "FindClipsDb.db").build()

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
    abstract fun spotifyRemoteDataStore(ds: SpotifyRemoteRepo): ISpotifyRemoteDataStore

    @Binds
    abstract fun spotifyDbDataStore(ds: SpotifyLocalRepo): ISpotifyLocalRepo

    @Binds
    abstract fun videosRemoteDataStore(ds: VideosRemoteDataStore): IVideosRemoteDataStore

    @Binds
    abstract fun videosDbDataStore(ds: VideosDbDataStore): IVideosDbDataStore

    @Binds
    abstract fun soundCloudDbDataStore(ds: SoundCloudDbDataStore): ISoundCloudDbDataStore

    @Binds
    abstract fun soundCloudRemoteDataStore(ds: SoundCloudRemoteDataStore): ISoundCloudRemoteDataStore
}