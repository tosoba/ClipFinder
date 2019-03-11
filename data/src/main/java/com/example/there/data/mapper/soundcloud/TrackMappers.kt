package com.example.there.data.mapper.soundcloud

import com.example.there.data.entity.soundcloud.TrackId
import com.example.there.domain.entity.soundcloud.SoundCloudTrackEntity
import com.example.there.domain.entity.soundcloud.SoundCloudTrackIdEntity
import com.vpaliy.soundcloud.model.TrackEntity

val TrackId.domain: SoundCloudTrackIdEntity
    get() = SoundCloudTrackIdEntity(id, kind)

val SoundCloudTrackIdEntity.data: TrackId
    get() = TrackId(id, kind)

val TrackEntity.domain: SoundCloudTrackEntity
    get() = SoundCloudTrackEntity(
            id,
            created_at,
            title,
            artwork_url,
            description,
            duration,
            genre,
            tags_list,
            stream_url,
            download_url,
            waveform_url
    )