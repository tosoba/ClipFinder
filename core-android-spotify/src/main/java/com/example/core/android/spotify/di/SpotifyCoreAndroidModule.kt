package com.example.core.android.spotify.di

import com.clipfinder.core.spotify.auth.ISpotifyAuth
import com.clipfinder.core.spotify.repo.ISpotifyRepo
import com.clipfinder.core.spotify.token.AccessTokenHolder
import com.clipfinder.core.spotify.token.SpotifyTokensHolder
import com.example.core.android.spotify.api.SpotifyAuth
import com.example.core.android.spotify.preferences.SpotifyPreferences
import com.example.core.android.spotify.repo.SpotifyRepo
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.bind
import org.koin.dsl.binds
import org.koin.dsl.module

val spotifyCoreAndroidModule = module {
    single { SpotifyPreferences(androidContext()) } binds arrayOf(AccessTokenHolder::class, SpotifyTokensHolder::class)
    single { SpotifyAuth(get(), get(), get()) } bind ISpotifyAuth::class
    single { SpotifyRepo(get(), get()) } bind ISpotifyRepo::class
}
