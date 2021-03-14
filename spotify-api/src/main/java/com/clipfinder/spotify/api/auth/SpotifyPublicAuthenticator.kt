package com.clipfinder.spotify.api.auth

import ClipFinder.BuildConfig
import com.clipfinder.core.ext.mapSuccess
import com.clipfinder.core.spotify.token.SpotifyTokensHolder
import com.clipfinder.spotify.api.endpoint.TokenEndpoints
import com.clipfinder.spotify.api.model.GrantType
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route

class SpotifyPublicAuthenticator(
    private val authorization: String,
    private val tokensHolder: SpotifyTokensHolder,
    private val tokenEndpoints: TokenEndpoints
) : Authenticator {
    override fun authenticate(route: Route?, response: Response): Request = authenticatePublic(response)

    private fun authenticatePrivate(response: Response): Request {
        val tokenResponse = tokenEndpoints
            .getTokens(
                grantType = GrantType.REFRESH_TOKEN,
                refreshToken = tokensHolder.refreshToken,
                clientId = BuildConfig.SPOTIFY_CLIENT_ID
            )
            .mapSuccess()
            .blockingGet()

        tokensHolder.setPrivateTokens(
            accessToken = tokenResponse.accessToken,
            refreshToken = requireNotNull(tokenResponse.refreshToken) {
                "No refresh token present in TokenResponse."
            }
        )

        return response.request.authorizedWith(tokenResponse.accessToken)
    }

    private fun authenticatePublic(response: Response): Request {
        val accessToken = tokenEndpoints
            .getTokens(
                authorization = authorization,
                grantType = GrantType.CLIENT_CREDENTIALS
            )
            .mapSuccess()
            .blockingGet()
            .accessToken

        tokensHolder.setToken(accessToken)

        return response.request.authorizedWith(accessToken)
    }

    private fun Request.authorizedWith(accessToken: String): Request = newBuilder()
        .header("Authorization", "Bearer $accessToken")
        .build()
}