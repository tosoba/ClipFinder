package com.clipfinder.spotify.api.infrastructure

import com.clipfinder.spotify.api.models.EpisodeObject
import com.clipfinder.spotify.api.models.TrackObject
import com.clipfinder.spotify.api.models.TrackOrEpisodeObject
import com.clipfinder.spotify.api.models.TrackOrEpisodeType
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapters.PolymorphicJsonAdapterFactory
import com.squareup.moshi.adapters.Rfc3339DateJsonAdapter
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import java.util.*

object Serializer {
    @JvmStatic
    val moshiBuilder: Moshi.Builder = Moshi.Builder()
        .add(Date::class.java, Rfc3339DateJsonAdapter().nullSafe())
        .add(OffsetDateTimeAdapter())
        .add(LocalDateTimeAdapter())
        .add(LocalDateAdapter())
        .add(UUIDAdapter())
        .add(ByteArrayAdapter())
        .add(KotlinJsonAdapterFactory())
        .add(PolymorphicJsonAdapterFactory.of(TrackOrEpisodeObject::class.java, "type")
            .withSubtype(TrackObject::class.java, TrackOrEpisodeType.track.name)
            .withSubtype(EpisodeObject::class.java, TrackOrEpisodeType.episode.name))

    @JvmStatic
    val moshi: Moshi by lazy {
        moshiBuilder.build()
    }
}
