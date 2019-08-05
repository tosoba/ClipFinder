package com.example.spotifyapi.util

import com.example.spotifyapi.models.SimpleAlbum
import com.example.spotifyapi.models.SimplePlaylist
import com.example.spotifyapi.models.SpotifyCategory
import com.example.spotifyapi.models.SpotifyImage
import com.example.there.domain.entity.spotify.AlbumEntity
import com.example.there.domain.entity.spotify.CategoryEntity
import com.example.there.domain.entity.spotify.PlaylistEntity
import com.example.there.domain.entity.spotify.SimpleArtistEntity

private const val DEFAULT_ICON_URL = "https://t.scdn.co/media/derived/r-b-274x274_fd56efa72f4f63764b011b68121581d8_0_0_274_274.jpg"

object ChartTrackIdMapper {
    fun map(from: String): String = from.substring(from.lastIndexOf('/') + 1)
}

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
            userId = owner.id,
            uri = uri
    )