package com.example.there.findclips.model.mapper

import com.example.there.domain.entity.videos.VideoPlaylistEntity
import com.example.there.findclips.model.entity.videos.VideoPlaylist

val VideoPlaylistEntity.ui: VideoPlaylist
    get() = VideoPlaylist(
            id = id,
            name = name
    )

val VideoPlaylist.domain: VideoPlaylistEntity
    get() = VideoPlaylistEntity(
            id = id,
            name = name
    )