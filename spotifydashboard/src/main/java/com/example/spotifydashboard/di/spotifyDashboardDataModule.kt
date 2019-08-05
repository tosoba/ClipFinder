package com.example.spotifydashboard.di

import com.example.core.retrofit.clientWithInterceptors
import com.example.core.retrofit.interceptorWithHeaders
import com.example.core.retrofit.retrofitWith
import com.example.spotifydashboard.data.SpotifyDashboardRemoteRepo
import com.example.spotifydashboard.data.api.SpotifyDashboardApi
import com.example.spotifydashboard.data.api.SpotifyDashboardChartsApi
import com.example.spotifydashboard.domain.repo.ISpotifyDashboardRemoteRepo
import org.koin.dsl.bind
import org.koin.dsl.module
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

val spotifyDashboardDataModule = module {
    single {
        retrofitWith(
                url = spotifyApiUrl,
                client = clientWithInterceptors(interceptorWithHeaders(
                        "Accept" to "application/json",
                        "Content-Type" to "application/json"))
        ).create(SpotifyDashboardApi::class.java)
    }

    single {
        retrofitWith(
                url = spotifyChartsBaseUrl,
                converterFactory = ScalarsConverterFactory.create(),
                callAdapterFactories = arrayOf(RxJava2CallAdapterFactory.create())
        ).create(SpotifyDashboardChartsApi::class.java)
    }

    single { SpotifyDashboardRemoteRepo(get(), get(), get(), get(), get(), get()) } bind ISpotifyDashboardRemoteRepo::class
}

private const val spotifyApiUrl: String = "https://api.spotify.com/v1/"
private const val spotifyChartsBaseUrl: String = "https://spotifycharts.com/"
