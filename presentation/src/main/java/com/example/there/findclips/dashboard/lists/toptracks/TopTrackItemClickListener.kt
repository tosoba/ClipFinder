package com.example.there.findclips.dashboard.lists.toptracks

import com.example.there.domain.entities.spotify.TopTrackEntity

interface TopTrackItemClickListener {
    fun onClick(track: TopTrackEntity)
}