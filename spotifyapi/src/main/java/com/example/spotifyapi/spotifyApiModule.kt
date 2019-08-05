package com.example.spotifyapi

import com.example.core.retrofit.clientWithInterceptors
import com.example.core.retrofit.interceptorWithHeaders
import com.example.core.retrofit.retrofitWith
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import org.koin.dsl.module
import retrofit2.converter.moshi.MoshiConverterFactory

val spotifyApiModule = module {
    single {
        retrofitWith(
                url = spotifyApiBaseUrl,
                converterFactory = MoshiConverterFactory.create(Moshi.Builder()
                        .add(KotlinJsonAdapterFactory())
                        .build()),
                client = clientWithInterceptors(interceptorWithHeaders(
                        "Accept" to "application/json",
                        "Content-Type" to "application/json"))
        ).create(SpotifyBrowseApi::class.java)
    }
}

private const val spotifyApiBaseUrl: String = "https://api.spotify.com/v1/"
