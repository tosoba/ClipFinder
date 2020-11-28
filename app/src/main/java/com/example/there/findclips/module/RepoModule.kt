package com.example.there.findclips.module

import com.example.soundcloudrepo.SoundCloudRemoteDataStore
import com.example.there.domain.repo.soundcloud.ISoundCloudRemoteDataStore
import org.koin.dsl.bind
import org.koin.dsl.module

val repoModule = module {
    single { SoundCloudRemoteDataStore(get(), get()) } bind ISoundCloudRemoteDataStore::class
}