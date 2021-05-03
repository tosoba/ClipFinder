package com.clipfinder.core.android.spotify.di

import android.util.Base64
import com.clipfinder.core.android.spotify.BuildConfig
import com.clipfinder.core.android.spotify.auth.SpotifyAutoAuth
import com.clipfinder.core.android.spotify.auth.SpotifyManualAuth
import com.clipfinder.core.android.spotify.auth.SpotifyPrivateAuthenticator
import com.clipfinder.core.android.spotify.auth.SpotifyPublicAuthenticator
import com.clipfinder.core.android.spotify.preferences.SpotifyPreferences
import com.clipfinder.core.android.spotify.repo.SpotifyRepo
import com.clipfinder.core.spotify.auth.ISpotifyAutoAuth
import com.clipfinder.core.spotify.auth.ISpotifyPrivateAuthenticator
import com.clipfinder.core.spotify.auth.ISpotifyPublicAuthenticator
import com.clipfinder.core.spotify.auth.ISpotifyTokensHolder
import com.clipfinder.core.spotify.repo.ISpotifyRepo
import com.clipfinder.spotify.api.di.PlaylistsEndpointsType
import net.openid.appauth.AuthorizationService
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module

private const val spotifyAuthorizationQualifier = "SPOTIFY_AUTHORIZATION"

val spotifyCoreAndroidModule = module {
    single { SpotifyPreferences(androidContext()) } bind ISpotifyTokensHolder::class

    single(named(spotifyAuthorizationQualifier)) {
        "Basic ${Base64.encodeToString("${BuildConfig.SPOTIFY_CLIENT_ID}:${BuildConfig.SPOTIFY_CLIENT_SECRET}".toByteArray(), Base64.NO_WRAP)}"
    }

    single { SpotifyAutoAuth(get(), get()) } bind ISpotifyAutoAuth::class

    single {
        SpotifyRepo(
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(named(PlaylistsEndpointsType.PUBLIC.name)),
            get(named(PlaylistsEndpointsType.PRIVATE.name)),
            get(),
            get(),
            get()
        )
    } bind ISpotifyRepo::class

    single { AuthorizationService(androidContext()) }

    single { SpotifyManualAuth(get(), get()) }

    single {
        SpotifyPublicAuthenticator(get(named(spotifyAuthorizationQualifier)), get(), get())
    } bind ISpotifyPublicAuthenticator::class

    single { SpotifyPrivateAuthenticator(get(), get()) } bind ISpotifyPrivateAuthenticator::class
}
