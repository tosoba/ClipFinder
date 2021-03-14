package com.clipfinder.spotify.api.interceptor

import com.clipfinder.core.spotify.auth.ISpotifyTokensHolder
import okhttp3.Interceptor
import okhttp3.Response

class TokenInterceptor(
    private val isPrivate: Boolean,
    private val holder: ISpotifyTokensHolder
) : Interceptor {
    private val accessToken: String
        get() = requireNotNull(if (isPrivate) holder.privateAccessToken else holder.publicAccessToken) {
            "${if (isPrivate) "Private" else "Public"} access token is null."
        }

    override fun intercept(chain: Interceptor.Chain): Response = chain
        .proceed(
            chain.request()
                .newBuilder()
                .addHeader("Authorization", "Bearer $accessToken")
                .build()
        )
}
