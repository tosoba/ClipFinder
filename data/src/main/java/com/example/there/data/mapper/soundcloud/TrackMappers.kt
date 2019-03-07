package com.example.there.data.mapper.soundcloud

import com.example.there.data.entity.soundcloud.Track
import com.example.there.domain.entity.soundcloud.SoundCloudTrackEntity

val Track.domain: SoundCloudTrackEntity
    get() = SoundCloudTrackEntity(id, kind)

val SoundCloudTrackEntity.data: Track
    get() = Track(id, kind)