package com.clipfinder.core.android.soundcloud.di

import com.clipfinder.core.android.soundcloud.auth.SoundCloudAuth
import com.clipfinder.core.android.soundcloud.preferences.SoundCloudPreferences
import com.clipfinder.core.android.soundcloud.repo.SoundCloudRepo
import com.clipfinder.core.soundcloud.auth.ISoundCloudAuth
import com.clipfinder.core.soundcloud.preferences.ISoundCloudPreferences
import com.clipfinder.core.soundcloud.repo.ISoundCloudRepo
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.bind
import org.koin.dsl.module

val soundCloudCoreAndroidModule = module {
    single { SoundCloudPreferences(androidContext()) } bind ISoundCloudPreferences::class
    single { SoundCloudAuth(androidContext()) } bind ISoundCloudAuth::class
    single { SoundCloudRepo(get()) } bind ISoundCloudRepo::class
}
