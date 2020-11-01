package com.clipfinder.spotify.api.charts.di

import com.example.core.retrofit.retrofitWith
import com.clipfinder.spotify.api.charts.SpotifyChartsApi
import okhttp3.Interceptor
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

val spotifyChartsApiModule = module {
    single {
        retrofitWith(
            url = "https://spotifycharts.com/",
            client = get { parametersOf(emptyList<Interceptor>()) },
            converterFactory = ScalarsConverterFactory.create(),
            callAdapterFactories = arrayOf(RxJava2CallAdapterFactory.create())
        ).create(SpotifyChartsApi::class.java)
    }
}
