package com.example.there.findclips.module

import com.example.soundcloudrepo.SoundCloudRemoteDataStore
import com.example.there.domain.repo.soundcloud.ISoundCloudRemoteDataStore
import com.example.there.domain.repo.videos.IVideosRemoteDataStore
import com.example.videosrepo.VideosRemoteDataStore
import org.koin.dsl.bind
import org.koin.dsl.module

val repoModule = module {
    single { VideosRemoteDataStore(get()) } bind IVideosRemoteDataStore::class
    single { SoundCloudRemoteDataStore(get(), get()) } bind ISoundCloudRemoteDataStore::class
}