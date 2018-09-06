package com.example.there.domain.pages

import com.example.there.domain.entities.spotify.TrackEntity

data class TracksPage(
        val tracks: List<TrackEntity>,
        val offset: Int,
        val totalItems: Int
)