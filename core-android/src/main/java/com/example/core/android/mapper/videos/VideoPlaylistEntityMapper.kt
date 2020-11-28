package com.example.core.android.mapper.videos

import com.example.core.android.model.videos.VideoPlaylist
import com.example.there.domain.entity.videos.VideoPlaylistEntity

val VideoPlaylist.domain: VideoPlaylistEntity
    get() = VideoPlaylistEntity(id = id, name = name)
