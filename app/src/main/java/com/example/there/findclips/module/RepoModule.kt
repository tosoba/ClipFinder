package com.example.there.findclips.module

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
import org.koin.dsl.bind
import org.koin.dsl.module

val repoModule = module {
    single { SpotifyRemoteRepo(get(), get(), get(), get()) } bind ISpotifyRemoteDataStore::class
    single { SpotifyLocalRepo(get(), get(), get(), get(), get()) } bind ISpotifyLocalRepo::class
    single { VideosRemoteDataStore(get()) } bind IVideosRemoteDataStore::class
    single { VideosDbDataStore(get(), get(), get(), get()) } bind IVideosDbDataStore::class
    single { SoundCloudDbDataStore() } bind ISoundCloudDbDataStore::class
    single { SoundCloudRemoteDataStore(get(), get(), get()) } bind ISoundCloudRemoteDataStore::class
}