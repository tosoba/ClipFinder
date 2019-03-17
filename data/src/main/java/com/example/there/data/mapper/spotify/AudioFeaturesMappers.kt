package com.example.there.data.mapper.spotify

import com.example.spotifyapi.model.AudioFeaturesApiModel
import com.example.there.domain.entity.spotify.AudioFeaturesEntity

val AudioFeaturesApiModel.domain: AudioFeaturesEntity
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
