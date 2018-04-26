package com.example.there.data.response.spotify

import com.example.there.data.entities.spotify.PlaylistData
import com.google.gson.annotations.SerializedName

data class PlaylistsResponse(
        val message: String,
        @SerializedName("playlists") val result: PlaylistsResult
)

data class PlaylistsResult(
        val href: String,
        @SerializedName("items") val playlists: List<PlaylistData>
)