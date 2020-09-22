package com.clipfinder.spotify.api.interceptor

import com.clipfinder.core.spotify.token.AccessTokenHolder
import okhttp3.Interceptor
import okhttp3.Response

class TokenInterceptor(private val holder: AccessTokenHolder) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = requireNotNull(holder.token)
        val request = chain.request()
            .newBuilder()
            .addHeader("Authorization", "Bearer $token")
            .build()
        return chain.proceed(request)
    }
}
