package com.clipfinder.core.android.spotify.auth

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.clipfinder.core.android.spotify.preferences.SpotifyPreferences
import com.clipfinder.core.android.util.ext.scheduleSingleAlarm
import com.clipfinder.core.ext.mapSuccess
import com.clipfinder.core.spotify.auth.ISpotifyAutoAuth
import com.clipfinder.spotify.api.endpoint.TokenEndpoints
import com.clipfinder.spotify.api.model.GrantType
import io.reactivex.Completable
import io.reactivex.Single
import net.openid.appauth.AuthState
import net.openid.appauth.AuthorizationService

class SpotifyAutoAuth(
    private val context: Context,
    private val authorization: String,
    private val authService: AuthorizationService,
    private val preferences: SpotifyPreferences,
    private val tokenEndpoints: TokenEndpoints
) : ISpotifyAutoAuth {
    private val authorizePublicLock: Any = Any()
    private val authorizePrivateLock: Any = Any()

    override fun authorizePublic(): Completable = synchronized(authorizePublicLock) {
        Single.just(preferences.containPublicAccessToken)
            .flatMapCompletable { containToken ->
                if (containToken) {
                    Completable.complete()
                } else {
                    tokenEndpoints
                        .getTokens(
                            authorization = authorization,
                            grantType = GrantType.CLIENT_CREDENTIALS
                        )
                        .mapSuccess()
                        .doOnSuccess { response ->
                            preferences.publicAccessToken = response.accessToken
                            schedulePublicAccessTokenClearance(response.expiresIn)
                        }
                        .toCompletable()
                }
            }
    }

    private fun schedulePublicAccessTokenClearance(expiresInSeconds: Int) {
        context.scheduleSingleAlarm(
            timestampMillis = System.currentTimeMillis() + expiresInSeconds * 1000 - AuthState.EXPIRY_TIME_TOLERANCE_MS,
            pendingIntent = PendingIntent.getBroadcast(context, 1, Intent(), 0)
        )
    }

    override fun authorizePrivate(): Completable = synchronized(authorizePrivateLock) {
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
            } catch (ex: Exception) {
                emitter.onError(ex)
            }
        }
    }

    object NullAuthStateException : Throwable()
    object UnknownRefreshTokenRequestException : Throwable()
}
