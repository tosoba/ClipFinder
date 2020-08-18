package com.example.soundcloudrepo.mapper

import com.clipfinder.soundcloud.api.model.SoundCloudPlaylistApiModel
import com.clipfinder.soundcloud.api.model.SoundCloudSystemPlaylistApiModel
import com.clipfinder.soundcloud.api.model.SoundCloudTrackId
import com.example.there.domain.entity.soundcloud.SoundCloudPlaylistEntity
import com.example.there.domain.entity.soundcloud.SoundCloudSystemPlaylistEntity

val SoundCloudPlaylistApiModel.domain: SoundCloudPlaylistEntity
    get() = SoundCloudPlaylistEntity(
            artworkUrl = artworkUrl,
            createdAt = createdAt,
            displayDate = displayDate,
            duration = duration,
            id = id,
            isAlbum = isAlbum,
            kind = kind,
            likesCount = likesCount,
            permalink = permalink,
            permalinkUrl = permalinkUrl,
            `public` = `public`,
            publishedAt = publishedAt,
            repostsCount = repostsCount,
            title = title,
            trackCount = trackCount,
            uri = uri,
            user = user.domain,
            userId = userId
    )

val SoundCloudSystemPlaylistApiModel.domain: SoundCloudSystemPlaylistEntity
    get() = SoundCloudSystemPlaylistEntity(
            artworkUrl = artworkUrl,
            calculatedArtworkUrl = calculatedArtworkUrl,
            description = description,
            id = id,
            kind = kind,
            lastUpdated = lastUpdated,
            permalink = permalink,
            shortDescription = shortDescription,
            shortTitle = shortTitle,
            title = title,
            trackingFeatureName = trackingFeatureName,
            tracks = tracks.map(SoundCloudTrackId::domain)
    )