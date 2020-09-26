package com.clipfinder.spotify.api.di

import com.clipfinder.spotify.api.auth.SpotifyAuthenticator
import com.clipfinder.spotify.api.endpoint.*
import com.clipfinder.spotify.api.infrastructure.*
import com.clipfinder.spotify.api.model.EpisodeObject
import com.clipfinder.spotify.api.model.TrackObject
import com.clipfinder.spotify.api.model.TrackOrEpisodeObject
import com.clipfinder.spotify.api.model.TrackOrEpisodeType
import com.example.core.android.interceptor.CacheInterceptor
import com.example.core.android.interceptor.ConnectivityInterceptor
import com.example.core.retrofit.RxSealedCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapters.PolymorphicJsonAdapterFactory
import com.squareup.moshi.adapters.Rfc3339DateJsonAdapter
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.scope.Scope
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.*

val spotifyApiModule = module {
    val moshiConverterFactory = MoshiConverterFactory.create(
        Moshi.Builder()
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
    )

    single {
        Retrofit.Builder()
            .baseUrl("https://accounts.spotify.com/api/")
            .addConverterFactory(get<ScalarsConverterFactory>())
            .addConverterFactory(moshiConverterFactory)
            .addCallAdapterFactory(get<RxSealedCallAdapterFactory>())
            .addCallAdapterFactory(get<RxJava2CallAdapterFactory>())
            .client(
                OkHttpClient.Builder()
                    .addInterceptor(get<HttpLoggingInterceptor>())
                    .addInterceptor(get<ConnectivityInterceptor>())
                    .build()
            )
            .build()
            .create(AuthEndpoints::class.java)
    }

    fun <T> Scope.clientFor(endpointsClass: Class<T>) = Retrofit.Builder()
        .baseUrl("https://api.spotify.com/v1/")
        .addConverterFactory(get<ScalarsConverterFactory>())
        .addConverterFactory(moshiConverterFactory)
        .addCallAdapterFactory(get<RxSealedCallAdapterFactory>())
        .addCallAdapterFactory(get<RxJava2CallAdapterFactory>())
        .client(
            OkHttpClient.Builder()
                .addInterceptor(get<HttpLoggingInterceptor>())
                .addInterceptor(get<CacheInterceptor>())
                .authenticator(SpotifyAuthenticator(get(), get()))
                .build()
        )
        .build()
        .create(endpointsClass)

    single { clientFor(AlbumEndpoints::class.java) }
    single { clientFor(ArtistEndpoints::class.java) }
    single { clientFor(BrowseEndpoints::class.java) }
    single { clientFor(EpisodeEndpoints::class.java) }
    single { clientFor(FollowEndpoints::class.java) }
    single { clientFor(LibraryEndpoints::class.java) }
    single { clientFor(PersonalizationEndpoints::class.java) }
    single { clientFor(PlaylistsEndpoints::class.java) }
    single { clientFor(SearchEndpoints::class.java) }
    single { clientFor(ShowsEndpoints::class.java) }
    single { clientFor(TracksEndpoints::class.java) }
    single { clientFor(UserProfileEndpoints::class.java) }
}
