package com.example.spotifyrepo.mapper

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
