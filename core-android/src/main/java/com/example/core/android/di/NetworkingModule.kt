package com.example.core.android.di

import com.example.core.android.interceptor.CacheInterceptor
import com.example.core.android.interceptor.ConnectivityInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val coreAndroidNetworkingModule = module {
    single { ConnectivityInterceptor(androidContext()) }
    single { CacheInterceptor(androidContext()) }
}
