package com.clipfinder.core.spotify.model

import java.math.BigDecimal

interface ISpotifyAudioFeatures {
    val danceability: BigDecimal
    val energy: BigDecimal
    val loudness: BigDecimal
    val speechiness: BigDecimal
    val acousticness: BigDecimal
    val instrumentalness: BigDecimal
    val liveness: BigDecimal
    val tempo: BigDecimal
    val valence: BigDecimal
}
