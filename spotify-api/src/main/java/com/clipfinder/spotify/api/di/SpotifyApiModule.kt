package com.clipfinder.spotify.api.di

import com.clipfinder.spotify.api.endpoint.AuthEndpoints
import com.clipfinder.spotify.api.infrastructure.*
import com.clipfinder.spotify.api.model.EpisodeObject
import com.clipfinder.spotify.api.model.TrackObject
import com.clipfinder.spotify.api.model.TrackOrEpisodeObject
import com.clipfinder.spotify.api.model.TrackOrEpisodeType
import com.example.core.android.interceptor.ConnectivityInterceptor
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapters.PolymorphicJsonAdapterFactory
import com.squareup.moshi.adapters.Rfc3339DateJsonAdapter
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.*

//inline fun <reified T> spotiyEndpoints() =  Retrofit.Builder()
//    .baseUrl("https://api.spotify.com/v1/")
//    .addConverterFactory(ScalarsConverterFactory.create())
//    .addConverterFactory(MoshiConverterFactory.create(moshi))
//    .addCallAdapterFactory(RxSealedCallAdapterFactory.create())
//    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//    .client(
//        OkHttpClient.Builder()
//            .addInterceptor(loggingInterceptor)
//            //TODO:
//            .build()
//    )
//    .build()
//    .create(T::class.java)

val spotifyApiModule = module {
    val moshi = Moshi.Builder()
        .add(Date::class.java, Rfc3339DateJsonAdapter().nullSafe())
        .add(OffsetDateTimeAdapter())
        .add(LocalDateTimeAdapter())
        .add(LocalDateAdapter())
        .add(UUIDAdapter())
        .add(ByteArrayAdapter())
        .add(KotlinJsonAdapterFactory())
        .add(
            PolymorphicJsonAdapterFactory
                .of(TrackOrEpisodeObject::class.java, "type")
                .withSubtype(TrackObject::class.java, TrackOrEpisodeType.track.name)
                .withSubtype(EpisodeObject::class.java, TrackOrEpisodeType.episode.name)
        )
        .build()

    val loggingInterceptor = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC }

    single {
        Retrofit.Builder()
            .baseUrl("https://accounts.spotify.com/api/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(
                OkHttpClient.Builder()
                    .addInterceptor(loggingInterceptor)
                    .addInterceptor(get<ConnectivityInterceptor>())
                    .build()
            )
            .build()
            .create(AuthEndpoints::class.java)
    }
}
