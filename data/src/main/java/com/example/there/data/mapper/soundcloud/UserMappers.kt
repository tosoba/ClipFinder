package com.example.there.data.mapper.soundcloud

import com.example.there.data.entity.soundcloud.User
import com.example.there.domain.entity.soundcloud.SoundCloudUserEntity

val User.domain: SoundCloudUserEntity
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