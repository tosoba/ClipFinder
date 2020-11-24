package com.clipfinder.spotify.api.auth

import android.util.Base64
import com.clipfinder.core.spotify.token.SpotifyTokensHolder
import com.clipfinder.spotify.api.endpoint.TokenEndpoints
import com.clipfinder.spotify.api.model.GrantType
import com.example.core.ext.mapSuccess
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route

class SpotifyAuthenticator(
    private val clientId: String,
    private val clientSecret: String,
    private val tokensHolder: SpotifyTokensHolder,
    private val tokenEndpoints: TokenEndpoints
) : Authenticator {

    private val authorization: String
        get() = "Basic ${Base64.encodeToString("${clientId}:${clientSecret}".toByteArray(), Base64.NO_WRAP)}"

    override fun authenticate(route: Route?, response: Response): Request = if (tokensHolder.tokensPrivate) {
        authenticatePrivate(response)
    } else {
        authenticatePublic(response)
    }

    private fun authenticatePrivate(response: Response): Request {
        val tokenResponse = tokenEndpoints
            .getTokens(
                grantType = GrantType.REFRESH_TOKEN,
                refreshToken = tokensHolder.refreshToken,
                clientId = clientId
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