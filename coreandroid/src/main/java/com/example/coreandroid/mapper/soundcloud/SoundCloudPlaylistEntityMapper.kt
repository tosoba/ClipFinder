package com.example.coreandroid.mapper.soundcloud

import com.example.coreandroid.model.soundcloud.SoundCloudPlaylist
import com.example.coreandroid.model.soundcloud.SoundCloudSystemPlaylist
import com.example.there.domain.entity.soundcloud.SoundCloudPlaylistEntity
import com.example.there.domain.entity.soundcloud.SoundCloudSystemPlaylistEntity

val SoundCloudPlaylistEntity.ui: SoundCloudPlaylist
    get() = SoundCloudPlaylist(
            artworkUrl = artworkUrl,
            createdAt = createdAt,
            duration = duration,
            id = id,
            likesCount = likesCount,
            publishedAt = publishedAt,
            title = title,
            trackCount = trackCount,
            userId = userId
    )

val SoundCloudSystemPlaylistEntity.ui: SoundCloudSystemPlaylist
    get() = SoundCloudSystemPlaylist(
            artworkUrl = calculatedArtworkUrl,
            description = description,
            id = id,
            shortDescription = shortDescription,
            title = title,
            trackIds = tracks.map { it.id }
    )