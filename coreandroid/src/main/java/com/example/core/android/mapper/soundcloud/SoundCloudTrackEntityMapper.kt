package com.example.core.android.mapper.soundcloud

import com.example.core.android.model.soundcloud.SoundCloudTrack
import com.example.there.domain.entity.soundcloud.SoundCloudTrackEntity

val SoundCloudTrackEntity.ui: SoundCloudTrack
    get() = SoundCloudTrack(
        id = id,
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
