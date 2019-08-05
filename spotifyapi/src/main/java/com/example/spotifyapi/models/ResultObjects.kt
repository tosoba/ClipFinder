package com.example.spotifyapi.models

import com.example.spotifyapi.model.PagedResponse
import com.google.gson.annotations.SerializedName

class ExternalUrl(val name: String, val url: String)

/**
 * An external id linked to the result object
 *
 * @property key The identifier type, for example:
- "isrc" - International Standard Recording Code
- "ean" - International Article Number
- "upc" - Universal Product Code
 * @property id An external identifier for the object.
 */
class ExternalId(val key: String, val id: String)


interface ResultEnum {
    fun retrieveIdentifier(): Any
}

/**
 * Wraps around [ErrorObject]
 */
data class ErrorResponse(val error: ErrorObject)

/**
 * An endpoint exception from Spotify
 *
 * @property status The HTTP status code
 * @property message A short description of the cause of the error.
 */
data class ErrorObject(val status: Int, val message: String)

data class PlaylistsResponse(
        @SerializedName("playlists") val result: PagingObject<SimplePlaylist>
) : PagedResponse<SimplePlaylist> {
    override val items: List<SimplePlaylist> get() = result.items
    override val offset: Int get() = result.offset
    override val totalItems: Int get() = result.total
}

data class CategoriesResponse(
        @SerializedName("categories") val result: PagingObject<SpotifyCategory>
) : PagedResponse<SpotifyCategory> {
    override val items: List<SpotifyCategory> get() = result.items
    override val offset: Int get() = result.offset
    override val totalItems: Int get() = result.total
}

data class AlbumsResponse(
        @SerializedName("albums") val result: PagingObject<SimpleAlbum>
) : PagedResponse<SimpleAlbum> {
    override val items: List<SimpleAlbum> get() = result.items
    override val offset: Int get() = result.offset
    override val totalItems: Int get() = result.total
}
