package com.example.there.findclips.module

import com.example.api.SoundCloudApi
import com.example.api.SoundCloudApiV2
import com.example.api.SoundCloudAuth
import com.example.core.retrofit.clientWithInterceptors
import com.example.core.retrofit.interceptorWithHeaders
import com.example.core.retrofit.retrofitWith
import com.example.spotifyapi.SpotifyAccountsApi
import com.example.spotifyapi.SpotifyApi
import com.example.youtubeapi.YoutubeApi
import com.vpaliy.soundcloud.SoundCloud
import org.koin.dsl.module
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory

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
private const val youtubeBaseUrl: String = "https://www.googleapis.com/youtube/v3/"
private const val soundCloudBaseUrlV2: String = "https://api-v2.soundcloud.com/"
private const val soundCloudBaseUrl: String = "https://api.soundcloud.com/"
