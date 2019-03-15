package com.example.there.findclips.model.mapper.soundcloud

import com.example.there.domain.entity.soundcloud.SoundCloudPlaylistEntity
import com.example.there.domain.entity.soundcloud.SoundCloudSystemPlaylistEntity
import com.example.there.findclips.model.entity.soundcloud.SoundCloudPlaylist
import com.example.there.findclips.model.entity.soundcloud.SoundCloudSystemPlaylist

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