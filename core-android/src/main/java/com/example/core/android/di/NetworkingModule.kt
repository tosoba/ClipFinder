package com.example.core.android.di

import com.clipfinder.core.retrofit.RxSealedCallAdapterFactory
import com.example.core.android.interceptor.CacheInterceptor
import com.example.core.android.interceptor.ConnectivityInterceptor
import okhttp3.Cache
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

val coreAndroidNetworkingModule = module {
    single { Cache(androidContext().cacheDir, 10_000_000L) }

    single { ConnectivityInterceptor(androidContext()) }
    single { CacheInterceptor(androidContext()) }
    single { HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC } }

    single { GsonConverterFactory.create() }

    single { ScalarsConverterFactory.create() }
    single { RxSealedCallAdapterFactory.create() }
    single { RxJava2CallAdapterFactory.create() }
}
