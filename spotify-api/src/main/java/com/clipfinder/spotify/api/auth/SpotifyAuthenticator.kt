package com.clipfinder.spotify.api.auth

import com.clipfinder.core.spotify.auth.SpotifyAuthData
import com.clipfinder.core.spotify.token.AccessTokenHolder
import com.clipfinder.core.spotify.token.RefreshTokenHolder
import com.clipfinder.spotify.api.endpoint.AuthEndpoints
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route

class SpotifyAuthenticator(
    private val accessTokenHolder: AccessTokenHolder,
    private val refreshTokenHolder: RefreshTokenHolder,
    private val auth: AuthEndpoints
) : Authenticator {
    override fun authenticate(route: Route?, response: Response): Request? {
        val refreshTokenResponse = auth.refreshToken(
            grantType = REFRESH_TOKEN_GRANT_TYPE,
            refreshToken = requireNotNull(refreshTokenHolder.refreshToken),
            clientId = SpotifyAuthData.CLIENT_ID
        ).execute()

        if (!refreshTokenResponse.isSuccessful) {
            // AppAnalytics?
            return null
        }

        val responseBody = requireNotNull(refreshTokenResponse.body()) {
            "RefreshToken response body is null."
        }

        accessTokenHolder.token = responseBody.accessToken
        refreshTokenHolder.refreshToken = responseBody.refreshToken

        return response.request.newBuilder()
            .header("Authorization", "Bearer ${responseBody.accessToken}")
            .build()
    }

    companion object {
        private const val REFRESH_TOKEN_GRANT_TYPE = "refresh_token"
    }
}