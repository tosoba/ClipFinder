package com.example.spotifyapi.di

import com.example.core.retrofit.clientWithInterceptors
import com.example.core.retrofit.interceptorWithHeaders
import com.example.core.retrofit.retrofitWith
import com.example.spotifyapi.*
import okhttp3.Interceptor
import org.koin.core.parameter.parametersOf
import org.koin.core.scope.Scope
import org.koin.dsl.module
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

private inline fun <reified API> Scope.spotifyRetrofit(): API = retrofitWith(
    url = spotifyApiBaseUrl,
    client = get {
        parametersOf(
            listOf(
                interceptorWithHeaders(
                    "Accept" to "application/json",
                    "Content-Type" to "application/json"
                )
            )
        )
    }
).create(API::class.java)

val spotifyApiModule = module {
    single {
        retrofitWith(
            url = spotifyAccountsApiBaseUrl,
            client = clientWithInterceptors(
                interceptorWithHeaders("Content-Type" to "application/x-www-form-urlencoded")
            )
        ).create(SpotifyAccountsApi::class.java)
    }
    single { spotifyRetrofit<SpotifyAlbumsApi>() }
    single { spotifyRetrofit<SpotifyArtistsApi>() }
    single { spotifyRetrofit<SpotifyBrowseApi>() }
    single { spotifyRetrofit<SpotifyPersonalizationApi>() }
    single { spotifyRetrofit<SpotifySearchApi>() }
    single { spotifyRetrofit<SpotifyTracksApi>() }
    single { spotifyRetrofit<SpotifyUsersApi>() }

    single {
        retrofitWith(
            url = spotifyChartsBaseUrl,
            client = get { parametersOf(emptyList<Interceptor>()) },
            converterFactory = ScalarsConverterFactory.create(),
            callAdapterFactories = arrayOf(RxJava2CallAdapterFactory.create())
        ).create(SpotifyChartsApi::class.java)
    }
}

private const val spotifyAccountsApiBaseUrl: String = "https://accounts.spotify.com/api/"
private const val spotifyApiBaseUrl: String = "https://api.spotify.com/v1/"
private const val spotifyChartsBaseUrl: String = "https://spotifycharts.com/"
