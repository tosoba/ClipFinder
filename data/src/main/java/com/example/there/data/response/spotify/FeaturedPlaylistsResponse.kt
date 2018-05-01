package com.example.there.data.response.spotify

import com.example.there.data.entities.spotify.PlaylistData
import com.google.gson.annotations.SerializedName

data class FeaturedPlaylistsResponse(
        val message: String,
        @SerializedName("playlists") val result: FeaturedPlaylistsResult
)

data class FeaturedPlaylistsResult(
        val href: String,
        @SerializedName("items") val playlists: List<PlaylistData>
)