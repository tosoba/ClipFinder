package com.example.soundcloudrepo.mapper

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