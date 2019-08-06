package com.example.spotifyapi

import com.example.core.retrofit.interceptorWithHeaders
import com.example.core.retrofit.retrofitWith
import okhttp3.Interceptor
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

val spotifyApiModule = module {
    single {
        retrofitWith(
                url = spotifyApiBaseUrl,
                client = get {
                    parametersOf(listOf(interceptorWithHeaders(
                            "Accept" to "application/json",
                            "Content-Type" to "application/json")))
                }
        ).create(SpotifyBrowseApi::class.java)
    }

    single {
        retrofitWith(
                url = spotifyChartsBaseUrl,
                client = get { parametersOf(emptyList<Interceptor>()) },
                converterFactory = ScalarsConverterFactory.create(),
                callAdapterFactories = arrayOf(RxJava2CallAdapterFactory.create())
        ).create(SpotifyChartsApi::class.java)
    }
}

private const val spotifyApiBaseUrl: String = "https://api.spotify.com/v1/"
private const val spotifyChartsBaseUrl: String = "https://spotifycharts.com/"
