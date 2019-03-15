package com.example.there.findclips.model.mapper.soundcloud

import com.example.there.domain.entity.soundcloud.SoundCloudTrackEntity
import com.example.there.findclips.model.entity.soundcloud.SoundCloudTrack

val SoundCloudTrackEntity.ui: SoundCloudTrack
    get() = SoundCloudTrack(
            id = id,
            createdAt = createdAt,
            title = title,
            artworkUrl = artworkUrl,
            description = description,
            duration = duration,
            genre = genre,
            tags = tags,
            streamUrl = streamUrl,
            downloadUrl = downloadUrl,
            waveformUrl = waveformUrl
    )

val SoundCloudTrack.domain: SoundCloudTrackEntity
    get() = SoundCloudTrackEntity(
            id = id,
            createdAt = createdAt,
            title = title,
            artworkUrl = artworkUrl,
            description = description,
            duration = duration,
            genre = genre,
            tags = tags,
            streamUrl = streamUrl,
            downloadUrl = downloadUrl,
            waveformUrl = waveformUrl
    )