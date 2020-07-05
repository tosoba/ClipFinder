package com.example.coreandroid.mapper.videos

import com.example.coreandroid.model.videos.VideoPlaylist
import com.example.there.domain.entity.videos.VideoPlaylistEntity

val VideoPlaylistEntity.ui: VideoPlaylist
    get() = VideoPlaylist(id = id, name = name)

val VideoPlaylist.domain: VideoPlaylistEntity
    get() = VideoPlaylistEntity(id = id, name = name)
