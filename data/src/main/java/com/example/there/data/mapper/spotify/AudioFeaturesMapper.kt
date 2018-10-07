package com.example.there.data.mapper.spotify

import com.example.there.data.entity.spotify.AudioFeaturesData
import com.example.there.domain.common.OneWayMapper
import com.example.there.domain.entity.spotify.AudioFeaturesEntity

object AudioFeaturesMapper : OneWayMapper<AudioFeaturesData, AudioFeaturesEntity>() {
    override fun mapFrom(from: AudioFeaturesData): AudioFeaturesEntity {
        return AudioFeaturesEntity(
                danceability = from.danceability,
                acousticness = from.acousticness,
                energy = from.energy,
                speechiness = from.speechiness,
                liveness = from.liveness,
                loudness = from.loudness,
                instrumentalness = from.instrumentalness,
                tempo = from.tempo,
                valence = from.valence
        )
    }
}