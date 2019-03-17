package com.example.soundcloudrepo.mapper

import com.example.api.model.SoundCloudUserApiModel
import com.example.there.domain.entity.soundcloud.SoundCloudUserEntity

val SoundCloudUserApiModel.domain: SoundCloudUserEntity
    get() = SoundCloudUserEntity(
            avatarUrl = avatarUrl,
            firstName = firstName,
            fullName = fullName,
            id = id,
            kind = kind,
            lastModified = lastModified,
            lastName = lastName,
            permalink = permalink,
            permalinkUrl = permalinkUrl,
            uri = uri,
            urn = urn,
            username = username,
            verified = verified
    )