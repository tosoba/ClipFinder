package com.example.core.android.spotify.di

import android.net.Uri
import com.clipfinder.core.spotify.auth.ISpotifyAutoAuth
import com.clipfinder.core.spotify.repo.ISpotifyRepo
import com.clipfinder.core.spotify.token.AccessTokenHolder
import com.clipfinder.core.spotify.token.SpotifyTokensHolder
import com.example.core.android.spotify.R
import com.example.core.android.spotify.auth.SpotifyAutoAuth
import com.example.core.android.spotify.auth.SpotifyManualAuth
import com.example.core.android.spotify.preferences.SpotifyPreferences
import com.example.core.android.spotify.repo.SpotifyRepo
import net.openid.appauth.AuthorizationService
import net.openid.appauth.AuthorizationServiceConfiguration
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.bind
import org.koin.dsl.binds
import org.koin.dsl.module

val spotifyCoreAndroidModule = module {
    single {
        SpotifyPreferences(androidContext())
    } binds arrayOf(AccessTokenHolder::class, SpotifyTokensHolder::class)

    single {
        val context = androidContext()
        val id = context.getString(R.string.spotify_client_id)
        val secret = context.getString(R.string.spotify_client_secret)
        SpotifyAutoAuth(id, secret, get(), get())
    } bind ISpotifyAutoAuth::class

    single {
        SpotifyRepo(get(), get(), get(), get(), get(), get(), get(), get(), get(), get(), get())
    } bind ISpotifyRepo::class

    single {
        val context = androidContext()
        SpotifyManualAuth(
            clientId = context.getString(R.string.spotify_client_id),
            redirectUri = context.getString(R.string.spotify_redirect_uri),
            scopes = context.resources.getStringArray(R.array.spotify_scopes),
            authService = AuthorizationService(context),
            authServiceConfig = AuthorizationServiceConfiguration(
                Uri.parse("https://accounts.spotify.com/authorize"),
                Uri.parse("https://accounts.spotify.com/api/token")
            ),
            preferences = get()
        )
    }
}
