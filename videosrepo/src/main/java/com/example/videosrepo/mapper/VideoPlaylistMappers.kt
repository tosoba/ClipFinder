package com.example.videosrepo.mapper

import com.example.db.model.videos.VideoPlaylistDbModel
import com.example.there.domain.entity.videos.VideoPlaylistEntity

val VideoPlaylistDbModel.domain: VideoPlaylistEntity
    get() = VideoPlaylistEntity(
        id = id,
        name = name
    )

val VideoPlaylistEntity.data: VideoPlaylistDbModel
    get() = VideoPlaylistDbModel(
        id = id,
        name = name
    )
