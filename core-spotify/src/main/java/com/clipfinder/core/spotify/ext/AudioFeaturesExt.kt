package com.clipfinder.core.spotify.ext

import com.clipfinder.core.spotify.model.ISpotifyAudioFeatures
import java.math.BigDecimal
import java.util.*
import kotlin.reflect.KClass
import kotlin.reflect.KProperty1
import kotlin.reflect.full.declaredMemberProperties

val KClass<ISpotifyAudioFeatures>.decimalProps: List<KProperty1<ISpotifyAudioFeatures, BigDecimal>>
    get() =
        declaredMemberProperties
            .filterIsInstance<KProperty1<ISpotifyAudioFeatures, BigDecimal>>()
            .filter {
                it.name.lowercase(Locale.getDefault()) != "tempo" &&
                    it.name.lowercase(Locale.getDefault()) != "loudness"
            }
