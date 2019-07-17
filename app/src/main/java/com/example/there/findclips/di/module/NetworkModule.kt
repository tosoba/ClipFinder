package com.example.there.findclips.di.module

import android.content.Context
import com.example.api.SoundCloudApi
import com.example.api.SoundCloudApiV2
import com.example.api.SoundCloudAuth
import com.example.core.retrofit.clientWithInterceptors
import com.example.core.retrofit.interceptorWithHeaders
import com.example.core.retrofit.retrofitWith
import com.example.spotifyapi.SpotifyAccountsApi
import com.example.spotifyapi.SpotifyApi
import com.example.spotifyapi.SpotifyChartsApi
import com.example.there.findclips.di.Dependencies
import com.example.youtubeapi.YoutubeApi
import com.vpaliy.soundcloud.SoundCloud
import com.vpaliy.soundcloud.SoundCloudService
import dagger.Module
import dagger.Provides
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import javax.inject.Named

val apiModule = module {
    single {
        retrofitWith(
                url = spotifyApiBaseUrl,
                client = clientWithInterceptors(interceptorWithHeaders(
                        "Accept" to "application/json",
                        "Content-Type" to "application/json"))
        ).create(SpotifyApi::class.java)
    }
    single {
        retrofitWith(
                url = accessTokenBaseUrl,
                client = clientWithInterceptors(interceptorWithHeaders(
                        "Content-Type" to "application/x-www-form-urlencoded"))
        ).create(SpotifyAccountsApi::class.java)
    }
    single {
        retrofitWith(
                url = spotifyChartsBaseUrl,
                converterFactory = ScalarsConverterFactory.create(),
                callAdapterFactories = arrayOf(RxJava2CallAdapterFactory.create())
        ).create(SpotifyChartsApi::class.java)
    }
    single {
        retrofitWith(
                url = youtubeBaseUrl,
                callAdapterFactories = arrayOf(RxJava2CallAdapterFactory.create())
        ).create(YoutubeApi::class.java)
    }
    single {
        retrofitWith(
                url = soundCloudBaseUrl,
                callAdapterFactories = arrayOf(RxJava2CallAdapterFactory.create())
        ).create(SoundCloudApi::class.java)
    }
    single {
        retrofitWith(
                url = soundCloudBaseUrlV2,
                callAdapterFactories = arrayOf(RxJava2CallAdapterFactory.create())
        ).create(SoundCloudApiV2::class.java)
    }
    single { SoundCloud.create(SoundCloudAuth.key).createService(get()) }
}

private const val spotifyApiBaseUrl: String = "https://api.spotify.com/v1/"
private const val accessTokenBaseUrl: String = "https://accounts.spotify.com/api/"
private const val spotifyChartsBaseUrl: String = "https://spotifycharts.com/"
private const val youtubeBaseUrl: String = "https://www.googleapis.com/youtube/v3/"
private const val soundCloudBaseUrlV2: String = "https://api-v2.soundcloud.com/"
private const val soundCloudBaseUrl: String = "https://api.soundcloud.com/"

@Module
class NetworkModule {

    @Provides
    @Named(Dependencies.SPOTIFY_API_RETROFIT)
    fun spotifyApiRetrofit(): Retrofit = retrofitWith(
            url = spotifyApiBaseUrl,
            client = clientWithInterceptors(interceptorWithHeaders(
                    "Accept" to "application/json",
                    "Content-Type" to "application/json")))

    @Provides
    @Named(Dependencies.SPOTIFY_ACCOUNTS_API_RETROFIT)
    fun spotifyAccountsApiRetrofit(): Retrofit = retrofitWith(
            url = accessTokenBaseUrl,
            client = clientWithInterceptors(interceptorWithHeaders(
                    "Content-Type" to "application/x-www-form-urlencoded"))
    )

    @Provides
    @Named(Dependencies.SPOTIFY_CHARTS_RETROFIT)
    fun spotifyChartsRetrofit(): Retrofit = retrofitWith(
            url = spotifyChartsBaseUrl,
            converterFactory = ScalarsConverterFactory.create(),
            callAdapterFactories = arrayOf(RxJava2CallAdapterFactory.create())
    )

    @Provides
    @Named(Dependencies.YOUTUBE_API_RETROFIT)
    fun youtubeApiRetrofit(): Retrofit = retrofitWith(
            url = youtubeBaseUrl,
            callAdapterFactories = arrayOf(RxJava2CallAdapterFactory.create())
    )

    @Provides
    @Named(Dependencies.SOUNDCLOUD_API_RETROFIT)
    fun soundCloudApiRetrofit(): Retrofit = retrofitWith(
            url = soundCloudBaseUrl,
            callAdapterFactories = arrayOf(RxJava2CallAdapterFactory.create())
    )

    @Provides
    @Named(Dependencies.SOUNDCLOUD_API_V2_RETROFIT)
    fun soundCloudApiV2Retrofit(): Retrofit = retrofitWith(
            url = soundCloudBaseUrlV2,
            callAdapterFactories = arrayOf(RxJava2CallAdapterFactory.create())
    )

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
}