package com.example.spotifyapi

import com.example.core.retrofit.clientWithInterceptors
import com.example.core.retrofit.interceptorWithHeaders
import com.example.core.retrofit.retrofitWith
import org.koin.dsl.module
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

val spotifyApiModule = module {
    single {
        retrofitWith(
                url = spotifyApiBaseUrl,
                client = clientWithInterceptors(interceptorWithHeaders(
                        "Accept" to "application/json",
                        "Content-Type" to "application/json"))
        ).create(SpotifyBrowseApi::class.java)
    }

    single {
        retrofitWith(
                url = spotifyChartsBaseUrl,
                converterFactory = ScalarsConverterFactory.create(),
                callAdapterFactories = arrayOf(RxJava2CallAdapterFactory.create())
        ).create(SpotifyChartsApi::class.java)
    }
}

private const val spotifyApiBaseUrl: String = "https://api.spotify.com/v1/"
private const val spotifyChartsBaseUrl: String = "https://spotifycharts.com/"