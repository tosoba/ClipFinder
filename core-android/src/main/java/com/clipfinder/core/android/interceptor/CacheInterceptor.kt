package com.clipfinder.core.android.interceptor

import android.content.Context
import com.clipfinder.core.android.util.ext.isConnected
import okhttp3.Interceptor
import okhttp3.Response

class CacheInterceptor(
    private val context: Context,
    private val maxAge: Long = 300,
    private val maxStale: Long = 60 * 60 * 24 * 7
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response = chain
        .proceed(chain.request())
        .newBuilder()
        .header(
            "Cache-Control",
            if (context.isConnected) {
                "public, max-age=$maxAge"
            } else {
                "public, only-if-cached, max-stale=$maxStale"
            }
        )
        .removeHeader("Pragma")
        .build()
}
