package com.example.there.findclips.di.module

import android.content.Context
import com.example.api.SoundCloudApi
import com.example.api.SoundCloudApiV2
import com.example.api.SoundCloudAuth
import com.example.spotifyapi.SpotifyAccountsApi
import com.example.spotifyapi.SpotifyApi
import com.example.spotifyapi.SpotifyChartsApi
import com.example.there.findclips.di.Dependencies
import com.example.youtubeapi.YoutubeApi
import com.vpaliy.soundcloud.SoundCloud
import com.vpaliy.soundcloud.SoundCloudService
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import javax.inject.Named

@Module
class NetworkModule {

    @Provides
    @Named(Dependencies.SPOTIFY_API_RETROFIT)
    fun spotifyApiRetrofit(): Retrofit = Retrofit.Builder()
            .client(OkHttpClient().newBuilder().addInterceptor { chain ->
                chain.proceed(chain.request()
                        .newBuilder()
                        .addHeader("Accept", "application/json")
                        .addHeader("Content-Type", "application/json")
                        .build())
            }.build())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(spotifyApiBaseUrl)
            .build()

    @Provides
    @Named(Dependencies.SPOTIFY_ACCOUNTS_API_RETROFIT)
    fun spotifyAccountsApiRetrofit(): Retrofit = Retrofit.Builder()
            .client(OkHttpClient().newBuilder().addInterceptor { chain ->
                chain.proceed(chain.request()
                        .newBuilder()
                        .addHeader("Content-Type", "application/x-www-form-urlencoded")
                        .build())
            }.build())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(accessTokenBaseUrl)
            .build()

    @Provides
    @Named(Dependencies.SPOTIFY_CHARTS_RETROFIT)
    fun spotifyChartsRetrofit(): Retrofit = Retrofit.Builder()
            .client(OkHttpClient())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(ScalarsConverterFactory.create())
            .baseUrl(spotifyChartsBaseUrl)
            .build()

    @Provides
    @Named(Dependencies.YOUTUBE_API_RETROFIT)
    fun youtubeApiRetrofit(): Retrofit = buildDefaultRetrofitWithUrl(youtubeBaseUrl)

    @Provides
    @Named(Dependencies.SOUNDCLOUD_API_RETROFIT)
    fun soundCloudApiRetrofit(): Retrofit = buildDefaultRetrofitWithUrl(soundCloudBaseUrl)

    @Provides
    @Named(Dependencies.SOUNDCLOUD_API_V2_RETROFIT)
    fun soundCloudApiV2Retrofit(): Retrofit = buildDefaultRetrofitWithUrl(soundCloudBaseUrlV2)

    @Provides
    fun spotifyApi(
            @Named(Dependencies.SPOTIFY_API_RETROFIT) retrofit: Retrofit
    ): SpotifyApi = retrofit.create(SpotifyApi::class.java)

    @Provides
    fun spotifyAccountsApi(
            @Named(Dependencies.SPOTIFY_ACCOUNTS_API_RETROFIT) retrofit: Retrofit
    ): SpotifyAccountsApi = retrofit.create(SpotifyAccountsApi::class.java)

    @Provides
    fun spotifyChartsApi(
            @Named(Dependencies.SPOTIFY_CHARTS_RETROFIT) retrofit: Retrofit
    ): SpotifyChartsApi = retrofit.create(SpotifyChartsApi::class.java)

    @Provides
    fun youtubeApi(
            @Named(Dependencies.YOUTUBE_API_RETROFIT) retrofit: Retrofit
    ): YoutubeApi = retrofit.create(YoutubeApi::class.java)

    @Provides
    fun soundCloudApi(
            @Named(Dependencies.SOUNDCLOUD_API_RETROFIT) retrofit: Retrofit
    ): SoundCloudApi = retrofit.create(SoundCloudApi::class.java)

    @Provides
    fun soundCloudApiV2(
            @Named(Dependencies.SOUNDCLOUD_API_V2_RETROFIT) retrofit: Retrofit
    ): SoundCloudApiV2 = retrofit.create(SoundCloudApiV2::class.java)

    @Provides
    fun soundCloudService(
            applicationContext: Context
    ): SoundCloudService = SoundCloud.create(SoundCloudAuth.key).createService(applicationContext)

    private fun buildDefaultRetrofitWithUrl(url: String): Retrofit = Retrofit.Builder()
            .client(OkHttpClient())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(url)
            .build()

    companion object {
        private const val spotifyApiBaseUrl: String = "https://api.spotify.com/v1/"
        private const val accessTokenBaseUrl: String = "https://accounts.spotify.com/api/"
        private const val spotifyChartsBaseUrl: String = "https://spotifycharts.com/"
        private const val youtubeBaseUrl: String = "https://www.googleapis.com/youtube/v3/"
        private const val soundCloudBaseUrlV2: String = "https://api-v2.soundcloud.com/"
        private const val soundCloudBaseUrl: String = "https://api.soundcloud.com/"
    }
}