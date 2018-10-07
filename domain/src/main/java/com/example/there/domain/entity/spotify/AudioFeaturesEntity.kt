package com.example.there.domain.entity.spotify

data class AudioFeaturesEntity(
        val danceability: Float,
        val energy: Float,
        val loudness: Float,
        val speechiness: Float,
        val acousticness: Float,
        val instrumentalness: Float,
        val liveness: Float,
        val tempo: Float,
        val valence: Float
)