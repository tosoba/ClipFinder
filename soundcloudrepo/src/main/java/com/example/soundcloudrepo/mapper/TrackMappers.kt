package com.example.soundcloudrepo.mapper

import com.example.api.SoundCloudAuth
import com.example.api.model.SoundCloudTrackApiModel
import com.example.api.model.SoundCloudTrackIdApiModel
import com.example.there.domain.entity.soundcloud.SoundCloudTrackEntity
import com.example.there.domain.entity.soundcloud.SoundCloudTrackIdEntity
import com.vpaliy.soundcloud.model.TrackEntity

val SoundCloudTrackIdApiModel.domain: SoundCloudTrackIdEntity
    get() = SoundCloudTrackIdEntity(id, kind)

val SoundCloudTrackIdEntity.data: SoundCloudTrackIdApiModel
    get() = SoundCloudTrackIdApiModel(id, kind)

val TrackEntity.domain: SoundCloudTrackEntity
    get() = SoundCloudTrackEntity(
            id = id,
            title = title,
            artworkUrl = artwork_url,
            description = description,
            duration = duration,
            genre = genre,
            tags = tags_list,
            streamUrl = when {
                stream_url == null -> null
                stream_url.endsWith("stream") -> "$stream_url/?client_id=${SoundCloudAuth.key}"
                else -> "$stream_url/stream?client_id=${SoundCloudAuth.key}"
            },
            downloadUrl = download_url,
            waveformUrl = waveform_url
    )

val SoundCloudTrackApiModel.domain: SoundCloudTrackEntity
    get() = SoundCloudTrackEntity(
            id = id.toString(),
            title = title,
            artworkUrl = artworkUrl,
            description = description,
            duration = duration.toString(),
            genre = genre,
            tags = tagList,
            streamUrl = if (uri.endsWith("stream")) "$uri?client_id=${SoundCloudAuth.key}"
            else "$uri/stream?client_id=${SoundCloudAuth.key}",
            downloadUrl = null,
            waveformUrl = waveformUrl
    )