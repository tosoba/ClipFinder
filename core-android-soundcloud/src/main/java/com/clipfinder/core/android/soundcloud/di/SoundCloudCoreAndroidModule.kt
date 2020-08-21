package com.clipfinder.core.android.soundcloud.di

import com.clipfinder.core.android.soundcloud.api.SoundCloudAuth
import com.clipfinder.core.android.soundcloud.preferences.SoundCloudPreferences
import com.clipfinder.core.soundcloud.api.ISoundCloudAuth
import com.clipfinder.core.soundcloud.preferences.ISoundCloudPreferences
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.bind
import org.koin.dsl.module

val soundCloudCoreAndroidModule = module {
    single { SoundCloudPreferences(androidContext()) } bind ISoundCloudPreferences::class
    single { SoundCloudAuth(androidContext()) } bind ISoundCloudAuth::class
}
