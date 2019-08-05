package com.example.spotifyapi.models

import com.google.gson.annotations.SerializedName

/**
 * Represents an album saved in a user's library
 *
 * @property addedAt The date and time the album was saved.
 * @property album Information about the album.
 */
data class SavedAlbum(
        @SerializedName("added_at") val addedAt: String,
        val album: Album
)

/**
 * Represents a track saved in a user's library
 *
 * @property addedAt The date and time the track was saved.
 * @property track The track object.
 */
data class SavedTrack(
        @SerializedName("added_at") val addedAt: String,
        val track: Track
)