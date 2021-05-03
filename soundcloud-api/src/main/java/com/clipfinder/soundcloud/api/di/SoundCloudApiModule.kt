package com.clipfinder.soundcloud.api.di

import com.clipfinder.soundcloud.api.SoundCloudApi
import com.clipfinder.soundcloud.api.SoundCloudApiV2
import okhttp3.OkHttpClient
import org.koin.core.scope.Scope
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

private inline fun <reified T> Scope.clientFor(baseUrl: String): T =
    Retrofit.Builder()
        .client(get<OkHttpClient>())
        .addConverterFactory(get<GsonConverterFactory>())
        .addCallAdapterFactory(get<RxJava2CallAdapterFactory>())
        .baseUrl(baseUrl)
        .build()
        .create(T::class.java)

val soundCloudApiModule = module {
    single { clientFor<SoundCloudApi>("https://api.soundcloud.com/") }
    single { clientFor<SoundCloudApiV2>("https://api-v2.soundcloud.com/") }
}
