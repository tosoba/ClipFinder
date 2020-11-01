package com.example.core.android.spotify.di

import com.clipfinder.core.spotify.auth.ISpotifyAuth
import com.clipfinder.core.spotify.repo.ISpotifyRepo
import com.clipfinder.core.spotify.token.AccessTokenHolder
import com.clipfinder.core.spotify.token.SpotifyTokensHolder
import com.example.core.android.spotify.R
import com.example.core.android.spotify.auth.SpotifyAuth
import com.example.core.android.spotify.preferences.SpotifyPreferences
import com.example.core.android.spotify.repo.SpotifyRepo
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.bind
import org.koin.dsl.binds
import org.koin.dsl.module

val spotifyCoreAndroidModule = module {
    single { SpotifyPreferences(androidContext()) } binds arrayOf(AccessTokenHolder::class, SpotifyTokensHolder::class)
    single {
        val context = androidContext()
        SpotifyAuth(
            context.getString(R.string.spotify_client_id),
            context.getString(R.string.spotify_client_secret),
            get(),
            get()
        )
    } bind ISpotifyAuth::class
    single { SpotifyRepo(get(), get(), get(), get(), get()) } bind ISpotifyRepo::class
}
