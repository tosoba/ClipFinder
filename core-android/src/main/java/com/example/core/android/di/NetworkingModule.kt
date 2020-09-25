package com.example.core.android.di

import com.example.core.android.interceptor.CacheInterceptor
import com.example.core.android.interceptor.ConnectivityInterceptor
import okhttp3.Cache
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val coreAndroidNetworkingModule = module {
    single { Cache(androidContext().cacheDir, 10 * 1000 * 1000) }

    single { ConnectivityInterceptor(androidContext()) }
    single { CacheInterceptor(androidContext()) }
}
