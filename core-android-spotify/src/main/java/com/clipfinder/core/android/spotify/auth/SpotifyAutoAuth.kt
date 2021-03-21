package com.clipfinder.core.android.spotify.auth

import com.clipfinder.core.android.spotify.preferences.SpotifyPreferences
import com.clipfinder.core.spotify.auth.ISpotifyAutoAuth
import io.reactivex.Completable
import net.openid.appauth.AuthState
import net.openid.appauth.AuthorizationService

class SpotifyAutoAuth(
    private val authService: AuthorizationService,
    private val preferences: SpotifyPreferences
) : ISpotifyAutoAuth {
    override fun authorizePrivate(): Completable =
        Completable.create { emitter ->
            val authState = preferences.authState
            if (authState == null) {
                emitter.onError(NullAuthStateException)
                return@create
            }

            try {
                authState.performActionWithFreshTokens(
                    authService,
                    AuthState.AuthStateAction { accessToken, _, ex ->
                        when {
                            ex != null -> emitter.onError(ex)
                            accessToken != null -> emitter.onComplete()
                            else -> emitter.onError(UnknownRefreshTokenRequestException)
                        }
                    }
                )
                preferences.authState = authState
            } catch (ex: Exception) {
                emitter.onError(ex)
            }
        }

    object NullAuthStateException : Throwable()
    object UnknownRefreshTokenRequestException : Throwable()
}
