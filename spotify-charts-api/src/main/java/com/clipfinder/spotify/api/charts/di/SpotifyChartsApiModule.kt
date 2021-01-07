package com.clipfinder.spotify.api.charts.di

import com.clipfinder.spotify.api.charts.ChartsEndpoints
import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

val spotifyChartsApiModule = module {
    single {
        Retrofit
            .Builder()
            .client(get<OkHttpClient>())
            .addConverterFactory(get<ScalarsConverterFactory>())
            .addCallAdapterFactory(get<RxJava2CallAdapterFactory>())
            .baseUrl("https://spotifycharts.com/")
            .build()
            .create(ChartsEndpoints::class.java)
    }
}
