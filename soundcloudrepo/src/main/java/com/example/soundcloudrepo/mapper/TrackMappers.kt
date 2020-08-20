package com.example.soundcloudrepo.mapper

import com.clipfinder.soundcloud.api.SoundCloudAuth
import com.clipfinder.soundcloud.api.model.SoundCloudTrack
import com.clipfinder.soundcloud.api.model.SoundCloudTrackId
import com.example.there.domain.entity.soundcloud.SoundCloudTrackEntity
import com.example.there.domain.entity.soundcloud.SoundCloudTrackIdEntity

val SoundCloudTrackId.domain: SoundCloudTrackIdEntity
    get() = SoundCloudTrackIdEntity(id, kind)

val SoundCloudTrackIdEntity.data: SoundCloudTrackId
    get() = SoundCloudTrackId(id, kind)

val SoundCloudTrack.domain: SoundCloudTrackEntity
    get() = SoundCloudTrackEntity(
        id = id.toString(),
        title = title,
        artworkUrl = artworkUrl,
        description = description,
        duration = duration,
        genre = genre,
        tags = tags,
        streamUrl = if (uri.endsWith("stream")) "$uri?client_id=${SoundCloudAuth.key}"
        else "$uri/stream?client_id=${SoundCloudAuth.key}",
        downloadUrl = null,
        waveformUrl = waveformUrl
    )