package com.example.there.domain.entitypage

import com.example.there.domain.entity.spotify.TrackEntity

data class TracksPage(
        val tracks: List<TrackEntity>,
        val offset: Int,
        val totalItems: Int
)