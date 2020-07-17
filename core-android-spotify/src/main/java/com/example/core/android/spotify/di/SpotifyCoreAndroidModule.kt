package com.example.core.android.spotify.di

import com.example.core.android.spotify.preferences.SpotifyPreferences
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val spotifyCoreAndroidModule = module {
    single { SpotifyPreferences(androidContext()) }
}
