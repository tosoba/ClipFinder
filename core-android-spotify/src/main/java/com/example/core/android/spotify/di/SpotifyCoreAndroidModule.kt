package com.example.core.android.spotify.di

import com.clipfinder.core.spotify.provider.AccessTokenProvider
import com.example.core.android.spotify.api.SpotifyAuth
import com.example.core.android.spotify.preferences.SpotifyPreferences
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.binds
import org.koin.dsl.module

val spotifyCoreAndroidModule = module {
    single { SpotifyPreferences(androidContext()) } binds arrayOf(AccessTokenProvider::class)
    single { SpotifyAuth(get(), get()) }
}
