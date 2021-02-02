package com.example.core.android.spotify.di

import com.clipfinder.core.spotify.auth.ISpotifyAutoAuth
import com.clipfinder.core.spotify.repo.ISpotifyRepo
import com.clipfinder.core.spotify.token.AccessTokenHolder
import com.clipfinder.core.spotify.token.SpotifyTokensHolder
import com.example.core.android.spotify.BuildConfig
import com.example.core.android.spotify.R
import com.example.core.android.spotify.auth.SpotifyAutoAuth
import com.example.core.android.spotify.auth.SpotifyManualAuth
import com.example.core.android.spotify.preferences.SpotifyPreferences
import com.example.core.android.spotify.repo.SpotifyRepo
import net.openid.appauth.AuthorizationService
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.bind
import org.koin.dsl.binds
import org.koin.dsl.module

val spotifyCoreAndroidModule = module {
    single {
        SpotifyPreferences(androidContext())
    } binds arrayOf(AccessTokenHolder::class, SpotifyTokensHolder::class)

    single {
        SpotifyAutoAuth(BuildConfig.SPOTIFY_CLIENT_ID, BuildConfig.SPOTIFY_CLIENT_SECRET, get(), get())
    } bind ISpotifyAutoAuth::class

    single {
        SpotifyRepo(get(), get(), get(), get(), get(), get(), get(), get(), get(), get(), get())
    } bind ISpotifyRepo::class

    single { SpotifyManualAuth(AuthorizationService(androidContext()), get()) }
}
