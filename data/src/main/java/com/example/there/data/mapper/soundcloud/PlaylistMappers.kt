package com.example.there.data.mapper.soundcloud

import com.example.there.data.entity.soundcloud.SoundCloudPlaylist
import com.example.there.data.entity.soundcloud.SoundCloudSystemPlaylist
import com.example.there.data.entity.soundcloud.Track
import com.example.there.domain.entity.soundcloud.SoundCloudPlaylistEntity
import com.example.there.domain.entity.soundcloud.SoundCloudSystemPlaylistEntity

val SoundCloudPlaylist.domain: SoundCloudPlaylistEntity
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

val SoundCloudSystemPlaylist.domain: SoundCloudSystemPlaylistEntity
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
            tracks = tracks.map(Track::domain)
    )