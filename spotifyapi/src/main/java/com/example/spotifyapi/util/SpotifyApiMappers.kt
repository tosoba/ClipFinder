package com.example.spotifyapi.util

import com.example.spotifyapi.models.*
import com.example.there.domain.entity.spotify.*

private const val DEFAULT_ICON_URL = "https://t.scdn.co/media/derived/r-b-274x274_fd56efa72f4f63764b011b68121581d8_0_0_274_274.jpg"

val List<SpotifyImage>.firstIconUrlOrDefault: String
    get() = getOrNull(0)?.url ?: DEFAULT_ICON_URL

val List<SpotifyImage>.secondIconUrlOrDefault: String
    get() = getOrNull(1)?.url ?: getOrNull(0)?.url ?: DEFAULT_ICON_URL

val SpotifyCategory.domain: CategoryEntity
    get() = CategoryEntity(
        id = id,
        name = name,
        iconUrl = icons.firstIconUrlOrDefault
    )

val SimpleAlbum.domain: AlbumEntity
    get() = AlbumEntity(
        id = id,
        name = name,
        albumType = albumType.name,
        artists = artists.map { SimpleArtistEntity(it.id, it.name) },
        iconUrl = images.secondIconUrlOrDefault,
        uri = uri
    )

val SimplePlaylist.domain: PlaylistEntity
    get() = PlaylistEntity(
        id = id,
        name = name,
        iconUrl = images.firstIconUrlOrDefault,
        userId = owner?.id,
        uri = uri
    )

val Track.domain: TrackEntity
    get() = TrackEntity(
        id = id,
        name = name,
        iconUrl = album.images.secondIconUrlOrDefault,
        albumId = album.id,
        albumName = album.name,
        artists = artists.map { SimpleArtistEntity(it.id, it.name) },
        popularity = popularity,
        trackNumber = trackNumber,
        uri = uri,
        durationMs = durationMs
    )
