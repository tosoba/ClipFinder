package com.example.core.android.spotify.di

import com.clipfinder.core.spotify.token.AccessTokenHolder
import com.clipfinder.core.spotify.token.SpotifyTokensHolder
import com.example.core.android.spotify.api.SpotifyAuth
import com.example.core.android.spotify.preferences.SpotifyPreferences
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.binds
import org.koin.dsl.module

val spotifyCoreAndroidModule = module {
    single { SpotifyPreferences(androidContext()) } binds arrayOf(AccessTokenHolder::class, SpotifyTokensHolder::class)
    single { SpotifyAuth(get(), get()) }
}
