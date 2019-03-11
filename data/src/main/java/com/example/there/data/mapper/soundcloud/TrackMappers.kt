package com.example.there.data.mapper.soundcloud

import com.example.there.data.entity.soundcloud.Track
import com.example.there.domain.entity.soundcloud.SoundCloudTrackEntity
import com.example.there.domain.entity.soundcloud.SoundCloudTrackIdEntity
import com.vpaliy.soundcloud.model.TrackEntity

val Track.domain: SoundCloudTrackIdEntity
    get() = SoundCloudTrackIdEntity(id, kind)

val SoundCloudTrackIdEntity.data: Track
    get() = Track(id, kind)

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