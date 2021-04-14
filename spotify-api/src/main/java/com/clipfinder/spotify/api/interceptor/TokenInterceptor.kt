package com.clipfinder.spotify.api.interceptor

import com.clipfinder.core.spotify.auth.ISpotifyTokensHolder
import okhttp3.Interceptor
import okhttp3.Response

class TokenInterceptor(
    private val isPrivate: Boolean,
    private val holder: ISpotifyTokensHolder
) : Interceptor {
    private val accessToken: String
        get() = (if (isPrivate) holder.privateAccessToken else holder.publicAccessToken)
            ?: "NO_TOKEN"

    override fun intercept(chain: Interceptor.Chain): Response = chain
        .proceed(
            chain.request()
                .newBuilder()
                .header("Authorization", "Bearer $accessToken")
                .build()
        )
}
