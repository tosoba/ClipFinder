package com.clipfinder.spotify.api.auth

import android.util.Base64
import com.clipfinder.core.spotify.auth.SpotifyAuthData
import com.clipfinder.core.spotify.token.SpotifyTokensHolder
import com.clipfinder.spotify.api.endpoint.TokenEndpoints
import com.clipfinder.spotify.api.model.GrantType
import com.clipfinder.spotify.api.model.TokensResponse
import com.example.core.retrofit.mapSuccess
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route

class SpotifyAuthenticator(
    private val tokensHolder: SpotifyTokensHolder,
    private val tokenEndpoints: TokenEndpoints
) : Authenticator {

    override fun authenticate(
        route: Route?, response: Response
    ): Request? = if (tokensHolder.tokensPrivate) {
        authenticatePrivate(response)
    } else {
        authenticatePublic(response)
    }

    private fun authenticatePrivate(response: Response): Request? {
        val tokenResponse: TokensResponse = tokenEndpoints
            .getTokens(
                grantType = GrantType.REFRESH_TOKEN,
                refreshToken = tokensHolder.refreshToken,
                clientId = SpotifyAuthData.CLIENT_ID
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

    private fun authenticatePublic(response: Response): Request? {
        val tokenResponse: TokensResponse = tokenEndpoints
            .getTokens(
                authorization = "Basic ${Base64.encodeToString("${SpotifyAuthData.CLIENT_ID}:${SpotifyAuthData.CLIENT_SECRET}".toByteArray(), Base64.NO_WRAP)}",
                grantType = GrantType.CLIENT_CREDENTIALS
            )
            .mapSuccess()
            .blockingGet()

        tokensHolder.setToken(tokenResponse.accessToken)

        return response.request.authorizedWith(tokenResponse.accessToken)
    }

    private fun Request.authorizedWith(accessToken: String): Request = newBuilder()
        .header("Authorization", "Bearer $accessToken")
        .build()
}