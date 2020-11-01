package com.example.spotifyapi.di

import com.example.core.retrofit.retrofitWith
import com.example.spotifyapi.SpotifyChartsApi
import okhttp3.Interceptor
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

val spotifyApiModule = module {
    single {
        retrofitWith(
            url = spotifyChartsBaseUrl,
            client = get { parametersOf(emptyList<Interceptor>()) },
            converterFactory = ScalarsConverterFactory.create(),
            callAdapterFactories = arrayOf(RxJava2CallAdapterFactory.create())
        ).create(SpotifyChartsApi::class.java)
    }
}

private const val spotifyChartsBaseUrl: String = "https://spotifycharts.com/"
