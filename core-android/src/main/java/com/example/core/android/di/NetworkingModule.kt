package com.example.core.android.di

import com.example.core.android.interceptor.CacheInterceptor
import com.example.core.android.interceptor.ConnectivityInterceptor
import com.example.core.retrofit.RxSealedCallAdapterFactory
import okhttp3.Cache
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

val coreAndroidNetworkingModule = module {
    single { Cache(androidContext().cacheDir, 10 * 1000 * 1000) }

    single { ConnectivityInterceptor(androidContext()) }
    single { CacheInterceptor(androidContext()) }
    single {
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC
        }
    }

    single { ScalarsConverterFactory.create() }

    single { RxSealedCallAdapterFactory.create() }
    single { RxJava2CallAdapterFactory.create() }
}
