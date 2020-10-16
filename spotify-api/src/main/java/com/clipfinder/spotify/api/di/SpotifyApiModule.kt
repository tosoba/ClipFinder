package com.clipfinder.spotify.api.di

import com.clipfinder.spotify.api.R
import com.clipfinder.spotify.api.auth.SpotifyAuthenticator
import com.clipfinder.spotify.api.endpoint.*
import com.clipfinder.spotify.api.infrastructure.*
import com.clipfinder.spotify.api.interceptor.TokenInterceptor
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
import org.koin.android.ext.koin.androidContext
import org.koin.core.scope.Scope
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.*

private inline fun <reified T> Scope.clientFor(): T = with(androidContext()) {
    Retrofit.Builder()
        .baseUrl("https://api.spotify.com/v1/")
        .addConverterFactory(get<ScalarsConverterFactory>())
        .addConverterFactory(get<MoshiConverterFactory>())
        .addCallAdapterFactory(get<RxSealedCallAdapterFactory>())
        .addCallAdapterFactory(get<RxJava2CallAdapterFactory>())
        .client(
            OkHttpClient.Builder()
                .addInterceptor(get<HttpLoggingInterceptor>())
                .addInterceptor(get<CacheInterceptor>())
                .addInterceptor(get<TokenInterceptor>())
                .authenticator(
                    SpotifyAuthenticator(
                        getString(R.string.spotify_client_id),
                        getString(R.string.spotify_client_secret),
                        get(),
                        get()
                    )
                )
                .build()
        )
        .build()
        .create(T::class.java)
}

val spotifyApiModule = module {
    single {
        MoshiConverterFactory.create(
            Moshi.Builder()
                .add(Date::class.java, Rfc3339DateJsonAdapter().nullSafe())
                .add(OffsetDateTimeAdapter)
                .add(UUIDAdapter)
                .add(ByteArrayAdapter)
                .add(KotlinJsonAdapterFactory())
                .add(
                    PolymorphicJsonAdapterFactory
                        .of(TrackOrEpisodeObject::class.java, "type")
                        .withSubtype(TrackObject::class.java, TrackOrEpisodeType.track.name)
                        .withSubtype(EpisodeObject::class.java, TrackOrEpisodeType.episode.name)
                )
                .build()
        )
    }

    single {
        Retrofit.Builder()
            .baseUrl("https://accounts.spotify.com/api/")
            .addConverterFactory(get<ScalarsConverterFactory>())
            .addConverterFactory(get<MoshiConverterFactory>())
            .addCallAdapterFactory(get<RxSealedCallAdapterFactory>())
            .addCallAdapterFactory(get<RxJava2CallAdapterFactory>())
            .client(
                OkHttpClient.Builder()
                    .addInterceptor(get<HttpLoggingInterceptor>())
                    .addInterceptor(get<ConnectivityInterceptor>())
                    .build()
            )
            .build()
            .create(TokenEndpoints::class.java)
    }

    single { TokenInterceptor(get()) }

    single { clientFor<AlbumEndpoints>() }
    single { clientFor<ArtistEndpoints>() }
    single { clientFor<BrowseEndpoints>() }
    single { clientFor<EpisodeEndpoints>() }
    single { clientFor<FollowEndpoints>() }
    single { clientFor<LibraryEndpoints>() }
    single { clientFor<PersonalizationEndpoints>() }
    single { clientFor<PlaylistsEndpoints>() }
    single { clientFor<SearchEndpoints>() }
    single { clientFor<ShowsEndpoints>() }
    single { clientFor<TracksEndpoints>() }
    single { clientFor<UserProfileEndpoints>() }
}
