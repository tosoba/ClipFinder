package com.clipfinder.spotify.api.auth

import com.clipfinder.core.spotify.auth.SpotifyAuthData
import com.clipfinder.core.spotify.token.SpotifyTokensHolder
import com.clipfinder.spotify.api.endpoint.AuthEndpoints
import com.clipfinder.spotify.api.model.GrantType
import com.clipfinder.spotify.api.model.TokensResponse
import com.example.core.retrofit.successOrThrow
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route

class SpotifyAuthenticator(
    private val tokensHolder: SpotifyTokensHolder,
    private val auth: AuthEndpoints
) : Authenticator {
    override fun authenticate(route: Route?, response: Response): Request? {
        val tokenResponse: TokensResponse = auth
            .getTokens(
                grantType = GrantType.REFRESH_TOKEN,
                refreshToken = tokensHolder.refreshToken,
                clientId = SpotifyAuthData.CLIENT_ID
            )
            .successOrThrow()
            .blockingGet()

        tokensHolder.setTokens(
            accessToken = tokenResponse.accessToken,
            refreshToken = tokenResponse.refreshToken
        )

        return response.request.newBuilder()
            .header("Authorization", "Bearer ${tokenResponse.accessToken}")
            .build()
    }
}