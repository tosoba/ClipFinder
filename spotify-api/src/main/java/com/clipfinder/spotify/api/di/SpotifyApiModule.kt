package com.clipfinder.spotify.api.di

import com.clipfinder.core.interceptor.ICacheInterceptor
import com.clipfinder.core.interceptor.IConnectivityInterceptor
import com.clipfinder.core.retrofit.RxSealedCallAdapterFactory
import com.clipfinder.core.spotify.auth.ISpotifyPrivateAuthenticator
import com.clipfinder.core.spotify.auth.ISpotifyPublicAuthenticator
import com.clipfinder.spotify.api.adapter.BigDecimalAdapter
import com.clipfinder.spotify.api.adapter.ByteArrayAdapter
import com.clipfinder.spotify.api.adapter.OffsetDateTimeAdapter
import com.clipfinder.spotify.api.adapter.UUIDAdapter
import com.clipfinder.spotify.api.endpoint.*
import com.clipfinder.spotify.api.interceptor.TokenInterceptor
import com.clipfinder.spotify.api.model.EpisodeObject
import com.clipfinder.spotify.api.model.PlaylistItemObject
import com.clipfinder.spotify.api.model.PlaylistItemType
import com.clipfinder.spotify.api.model.TrackObject
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapters.PolymorphicJsonAdapterFactory
import com.squareup.moshi.adapters.Rfc3339DateJsonAdapter
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.qualifier.named
import org.koin.core.scope.Scope
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.*

private const val PRIVATE_HTTP_CLIENT = "PRIVATE_HTTP_CLIENT"
private const val PUBLIC_HTTP_CLIENT = "PUBLIC_HTTP_CLIENT"

private const val PRIVATE_TOKEN_INTERCEPTOR = "PRIVATE_TOKEN_INTERCEPTOR"
private const val PUBLIC_TOKEN_INTERCEPTOR = "PUBLIC_TOKEN_INTERCEPTOR"

private fun Scope.httpClient(isPrivate: Boolean = false): OkHttpClient = OkHttpClient.Builder()
    .addInterceptor(get<HttpLoggingInterceptor>())
    .addInterceptor(get<ICacheInterceptor>())
    .addInterceptor(
        get<TokenInterceptor>(
            named(if (isPrivate) PRIVATE_TOKEN_INTERCEPTOR else PUBLIC_TOKEN_INTERCEPTOR)
        )
    )
    .authenticator(
        if (isPrivate) get<ISpotifyPrivateAuthenticator>() else get<ISpotifyPublicAuthenticator>()
    )
    .cache(get<Cache>())
    .build()

private inline fun <reified T> Scope.retrofitFor(isPrivate: Boolean = false): T = Retrofit.Builder()
    .baseUrl("https://api.spotify.com/v1/")
    .addConverterFactory(get<ScalarsConverterFactory>())
    .addConverterFactory(get<MoshiConverterFactory>())
    .addCallAdapterFactory(get<RxSealedCallAdapterFactory>())
    .addCallAdapterFactory(get<RxJava2CallAdapterFactory>())
    .client(get<OkHttpClient>(named(if (isPrivate) PRIVATE_HTTP_CLIENT else PUBLIC_HTTP_CLIENT)))
    .build()
    .create(T::class.java)

val spotifyApiModule = module {
    single(named(PRIVATE_HTTP_CLIENT)) { httpClient(true) }
    single(named(PUBLIC_HTTP_CLIENT)) { httpClient() }

    single {
        MoshiConverterFactory.create(
            Moshi.Builder()
                .add(Date::class.java, Rfc3339DateJsonAdapter().nullSafe())
                .add(OffsetDateTimeAdapter)
                .add(UUIDAdapter)
                .add(ByteArrayAdapter)
                .add(BigDecimalAdapter)
                .add(
                    PolymorphicJsonAdapterFactory
                        .of(PlaylistItemObject::class.java, "type")
                        .withSubtype(TrackObject::class.java, PlaylistItemType.track.name)
                        .withSubtype(EpisodeObject::class.java, PlaylistItemType.episode.name)
                )
                .add(KotlinJsonAdapterFactory())
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
                    .addInterceptor(get<IConnectivityInterceptor>())
                    .build()
            )
            .build()
            .create(TokenEndpoints::class.java)
    }

    single(named(PRIVATE_TOKEN_INTERCEPTOR)) { TokenInterceptor(true, get()) }
    single(named(PUBLIC_TOKEN_INTERCEPTOR)) { TokenInterceptor(false, get()) }

    single { retrofitFor<AlbumEndpoints>() }
    single { retrofitFor<ArtistEndpoints>() }
    single { retrofitFor<BrowseEndpoints>() }
    single { retrofitFor<EpisodeEndpoints>() }
    single { retrofitFor<FollowEndpoints>(true) }
    single { retrofitFor<LibraryEndpoints>() }
    single { retrofitFor<PersonalizationEndpoints>(true) }
    single { retrofitFor<PlaylistsEndpoints>() }
    single { retrofitFor<SearchEndpoints>() }
    single { retrofitFor<ShowsEndpoints>() }
    single { retrofitFor<TracksEndpoints>() }
    single { retrofitFor<UserProfileEndpoints>(true) }
}
