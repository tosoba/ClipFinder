package com.clipfinder.core.android.soundcloud.di

import com.clipfinder.core.android.soundcloud.preferences.SoundCloudPreferences
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val soundCloudCoreAndroidModule = module {
    single { SoundCloudPreferences(androidContext()) }
}
