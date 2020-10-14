package com.clipfinder.spotify.api.interceptor

import com.clipfinder.core.spotify.token.AccessTokenHolder
import okhttp3.Interceptor
import okhttp3.Response

class TokenInterceptor(private val holder: AccessTokenHolder) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response = chain
        .proceed(
            chain.request()
                .newBuilder()
                .addHeader("Authorization", "Bearer ${requireNotNull(holder.token)}")
                .build()
        )
}
