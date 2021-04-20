package com.clipfinder.core.android.di

import com.clipfinder.core.android.BuildConfig
import com.clipfinder.core.retrofit.RxSealedCallAdapterFactory
import com.clipfinder.core.android.interceptor.CacheInterceptor
import com.clipfinder.core.android.interceptor.ConnectivityInterceptor
import com.clipfinder.core.interceptor.ICacheInterceptor
import com.clipfinder.core.interceptor.IConnectivityInterceptor
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.bind
import org.koin.dsl.module
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

val coreAndroidNetworkingModule = module {
    single { Cache(androidContext().cacheDir, 10_000_000L) }

    single { ConnectivityInterceptor(androidContext()) } bind IConnectivityInterceptor::class
    single { CacheInterceptor(androidContext()) } bind ICacheInterceptor::class
    single {
        HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.BASIC
            }
        }
    }

    single { GsonConverterFactory.create() }

    single {
        OkHttpClient.Builder()
            .addInterceptor(get<HttpLoggingInterceptor>())
            .addInterceptor(get<CacheInterceptor>())
            .cache(get<Cache>())
            .build()
    }

    single { ScalarsConverterFactory.create() }
    single { RxSealedCallAdapterFactory.create() }
    single { RxJava2CallAdapterFactory.create() }
}
