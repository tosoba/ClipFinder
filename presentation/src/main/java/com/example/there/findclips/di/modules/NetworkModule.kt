package com.example.there.findclips.di.modules

import com.example.there.data.api.SpotifyAccountsApi
import com.example.there.data.api.SpotifyApi
import com.example.there.data.api.SpotifyChartsApi
import com.example.there.findclips.di.Dependencies
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
class NetworkModule(private val spotifyApiBaseUrl: String,
                    private val accessTokenBaseUrl: String,
                    private val spotifyChartsBaseUrl: String) {

    @Provides
    @Singleton
    @Named(Dependencies.SPOTIFY_API_RETROFIT)
    fun spotifyApiRetrofit(): Retrofit = Retrofit.Builder()
            .client(OkHttpClient().newBuilder().addInterceptor({ chain ->
                chain.proceed(chain.request()
                        .newBuilder()
                        .addHeader("Accept", "application/json")
                        .addHeader("Content-Type", "application/json")
                        .build())
            }).build())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(spotifyApiBaseUrl)
            .build()

    @Provides
    @Singleton
    @Named(Dependencies.SPOTIFY_ACCOUNTS_API_RETROFIT)
    fun spotifyAccountsApiRetrofit(): Retrofit = Retrofit.Builder()
            .client(OkHttpClient().newBuilder().addInterceptor({ chain ->
                chain.proceed(chain.request()
                        .newBuilder()
                        .addHeader("Content-Type", "application/x-www-form-urlencoded")
                        .build())
            }).build())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(accessTokenBaseUrl)
            .build()

    @Provides
    @Singleton
    @Named(Dependencies.SPOTIFY_CHARTS_RETROFIT)
    fun spotifyChartsRetrofit(): Retrofit = Retrofit.Builder()
            .client(OkHttpClient())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(ScalarsConverterFactory.create())
            .baseUrl(spotifyChartsBaseUrl)
            .build()

    @Provides
    @Singleton
    fun spotifyApi(@Named(Dependencies.SPOTIFY_API_RETROFIT) retrofit: Retrofit): SpotifyApi =
            retrofit.create(SpotifyApi::class.java)

    @Provides
    @Singleton
    fun spotifyAccountsApi(@Named(Dependencies.SPOTIFY_ACCOUNTS_API_RETROFIT) retrofit: Retrofit): SpotifyAccountsApi =
            retrofit.create(SpotifyAccountsApi::class.java)

    @Provides
    @Singleton
    fun spotifyChartsApi(@Named(Dependencies.SPOTIFY_CHARTS_RETROFIT) retrofit: Retrofit): SpotifyChartsApi =
            retrofit.create(SpotifyChartsApi::class.java)
}