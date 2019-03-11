package com.example.there.data.mapper.spotify

import com.example.there.data.entity.spotify.AudioFeaturesData
import com.example.there.domain.entity.spotify.AudioFeaturesEntity

val AudioFeaturesData.domain: AudioFeaturesEntity
    get() = AudioFeaturesEntity(
            danceability = danceability,
            acousticness = acousticness,
            energy = energy,
            speechiness = speechiness,
            liveness = liveness,
            loudness = loudness,
            instrumentalness = instrumentalness,
            tempo = tempo,
            valence = valence
    )
