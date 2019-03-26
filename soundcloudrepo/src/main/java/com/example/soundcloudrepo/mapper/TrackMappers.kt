package com.example.soundcloudrepo.mapper

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
            id,
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

val SoundCloudTrackApiModel.domain: SoundCloudTrackEntity
    get() = SoundCloudTrackEntity(
            id = id.toString(),
            title = title,
            artworkUrl = artworkUrl,
            description = description,
            duration = duration.toString(),
            genre = genre,
            tags = tagList,
            streamUrl = media.transcodings?.getOrNull(0)?.url, //TODO: check if it works when player is implemented
            downloadUrl = null,
            waveformUrl = waveformUrl
    )