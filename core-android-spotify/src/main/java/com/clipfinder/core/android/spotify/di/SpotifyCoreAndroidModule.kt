package com.clipfinder.core.android.spotify.di

import android.util.Base64
import com.clipfinder.core.spotify.auth.ISpotifyAutoAuth
import com.clipfinder.core.spotify.repo.ISpotifyRepo
import com.clipfinder.core.spotify.token.AccessTokenHolder
import com.clipfinder.core.spotify.token.SpotifyTokensHolder
import com.clipfinder.core.android.spotify.BuildConfig
import com.clipfinder.core.android.spotify.R
import com.clipfinder.core.android.spotify.auth.SpotifyAutoAuth
import com.clipfinder.core.android.spotify.auth.SpotifyManualAuth
import com.clipfinder.core.android.spotify.preferences.SpotifyPreferences
import com.clipfinder.core.android.spotify.repo.SpotifyRepo
import com.clipfinder.core.spotify.di.spotifyAuthorizationQualifier
import net.openid.appauth.AuthorizationService
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.binds
import org.koin.dsl.module

val spotifyCoreAndroidModule = module {
    single {
        SpotifyPreferences(androidContext())
    } binds arrayOf(AccessTokenHolder::class, SpotifyTokensHolder::class)

    single(named(spotifyAuthorizationQualifier)) {
        "Basic ${Base64.encodeToString("${BuildConfig.SPOTIFY_CLIENT_ID}:${BuildConfig.SPOTIFY_CLIENT_SECRET}".toByteArray(), Base64.NO_WRAP)}"
    }

    single {
        SpotifyAutoAuth(get(named(spotifyAuthorizationQualifier)), get(), get())
    } bind ISpotifyAutoAuth::class

    single {
        SpotifyRepo(get(), get(), get(), get(), get(), get(), get(), get(), get(), get(), get())
    } bind ISpotifyRepo::class

    single { SpotifyManualAuth(AuthorizationService(androidContext()), get()) }
}
