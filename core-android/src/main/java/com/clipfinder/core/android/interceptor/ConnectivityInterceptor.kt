package com.clipfinder.core.android.interceptor

import android.content.Context
import com.clipfinder.core.android.util.ext.isConnected
import com.clipfinder.core.interceptor.IConnectivityInterceptor
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class ConnectivityInterceptor(private val context: Context) : IConnectivityInterceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        if (!context.isConnected) throw IOException("No internet connection.")
        return chain.proceed(chain.request())
    }
}
