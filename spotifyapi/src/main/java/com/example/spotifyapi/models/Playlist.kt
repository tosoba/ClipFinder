package com.example.spotifyapi.models

import com.google.gson.annotations.SerializedName

/**
 * Simplified Playlist object that can be used to retrieve a full [Playlist]
 *
 * @property collaborative Returns true if context is not search and the owner allows other users to
 * modify the playlist. Otherwise returns false.
 * @property href A link to the Web API endpoint providing full details of the playlist.
 * @property id The Spotify ID for the playlist.
 * @property images Images for the playlist. The array may be empty or contain up to three images.
 * The images are returned by size in descending order. See Working with Playlists.
 * Note: If returned, the source URL for the image ( url ) is temporary and will expire in less than a day.
 * @property name The name of the playlist.
 * @property owner The user who owns the playlist
 * @property primaryColor Unknown.
 * @property public The playlist’s public/private status: true the playlist is public, false the
 * playlist is private, null the playlist status is not relevant.
 * @property tracks A collection containing a link ( href ) to the Web API endpoint where full details of the
 * playlist’s tracks can be retrieved, along with the total number of tracks in the playlist.
 * @property type The object type: “playlist”
 * @property snapshot The version identifier for the current playlist. Can be supplied in other
 * requests to target a specific playlist version
 */
data class SimplePlaylist(
    @SerializedName("external_urls") val externalUrls: Map<String, String>,
    @SerializedName("href") val href: String,
    val id: String,
    @SerializedName("uri") val uri: String,
    val collaborative: Boolean,
    val images: List<SpotifyImage>,
    val name: String,
    val owner: SpotifyPublicUser?,
    @SerializedName("primary_color") val primaryColor: String? = null,
    val public: Boolean? = null,
    @SerializedName("snapshot_id") private val _snapshotId: String,
    val tracks: PlaylistTrackInfo,
    val type: String
) {
    val snapshot: Snapshot get() = Snapshot(_snapshotId)
}

/**
 * Represents a Spotify track inside a [Playlist]
 *
 * @property primaryColor Unknown. Undocumented field
 * @property addedAt The date and time the track was added. Note that some very old playlists may return null in this field.
 * @property addedBy The Spotify user who added the track. Note that some very old playlists may return null in this field.
 * @property isLocal Whether this track is a local file or not.
 * @property track Information about the track.
 */
data class PlaylistTrack(
    @SerializedName("primary_color") val primaryColor: String? = null,
    @SerializedName("added_at") val addedAt: String?,
    @SerializedName("added_by") val addedBy: SpotifyPublicUser?,
    @SerializedName("is_local") val isLocal: Boolean?,
    val track: Track,
    @SerializedName("video_thumbnail") val videoThumbnail: VideoThumbnail? = null
)

/**
 * Represents a Playlist on Spotify
 *
 * @property collaborative Returns true if context is not search and the owner allows other users to modify the playlist.
 * Otherwise returns false.
 * @property description The playlist description. Only returned for modified, verified playlists, otherwise null.
 * @property followers
 * @property href A link to the Web API endpoint providing full details of the playlist.
 * @property id The Spotify ID for the playlist.
 * @property primaryColor Unknown.
 * @property images Images for the playlist. The array may be empty or contain up to three images.
 * The images are returned by size in descending order.Note: If returned, the source URL for the
 * image ( url ) is temporary and will expire in less than a day.
 * @property name The name of the playlist.
 * @property owner The user who owns the playlist
 * @property public The playlist’s public/private status: true the playlist is public, false the playlist is private,
 * null the playlist status is not relevant
 * @property snapshot The version identifier for the current playlist. Can be supplied in other requests to target
 * a specific playlist version
 * @property tracks Information about the tracks of the playlist.
 * @property type The object type: “playlist”
 */
data class Playlist(
    @SerializedName("external_urls") val externalUrls: Map<String, String>,
    @SerializedName("href") val href: String,
    val id: String,
    @SerializedName("uri") val uri: String,
    val collaborative: Boolean,
    val description: String,
    val followers: Followers,
    @SerializedName("primary_color") val primaryColor: String? = null,
    val images: List<SpotifyImage>,
    val name: String,
    val owner: SpotifyPublicUser?,
    val public: Boolean? = null,
    @SerializedName("snapshot_id") private val _snapshotId: String,
    val tracks: PagingObject<PlaylistTrack>,
    val type: String
) {
    val snapshot: Snapshot get() = Snapshot(_snapshotId)
}

/**
 * A collection containing a link ( href ) to the Web API endpoint where full details of the playlist’s tracks
 * can be retrieved, along with the total number of tracks in the playlist.
 *
 * @property href link to the Web API endpoint where full details of the playlist’s tracks
 * can be retrieved
 * @property total the total number of tracks in the playlist.
 */
data class PlaylistTrackInfo(
    val href: String,
    val total: Int
)

data class VideoThumbnail(val url: String?)

/**
 * Contains the snapshot id, returned from API responses
 *
 * @param snapshotId The playlist state identifier
 */
data class Snapshot(@SerializedName("snapshot_id") val snapshotId: String)
