package com.clipfinder.spotify.api.interceptor

import com.clipfinder.core.spotify.provider.AccessTokenProvider
import okhttp3.Interceptor
import okhttp3.Response

class TokenInterceptor(private val tokenProvider: AccessTokenProvider) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = requireNotNull(tokenProvider.token)
        val request = chain.request()
            .newBuilder()
            .addHeader("Authorization", "Bearer $token")
            .build()
        return chain.proceed(request)
    }
}
