/**
 * Spotify Web API No description provided (generated by Openapi Generator
 * https://github.com/openapitools/openapi-generator)
 *
 * The version of the OpenAPI document: 0.0.1
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech Do not edit the class manually.
 */
package com.clipfinder.spotify.api.model

import com.squareup.moshi.Json

/**
 *
 * @param collaborative true if the owner allows other users to modify the playlist.
 * @param externalUrls
 * @param href A link to the Web API endpoint providing full details of the playlist.
 * @param id The Spotify ID for the playlist.
 * @param images Images for the playlist. The array may be empty or contain up to three images. The
 * images are returned by size in descending order. See Working with Playlists. Note: If returned,
 * the source URL for the image (url) is temporary and will expire in less than a day.
 * @param name The name of the playlist.
 * @param owner
 * @param public The playlist’s public/private status: true the playlist is public, false the
 * playlist is private, null the playlist status is not relevant. For more about public/private
 * status, see Working with Playlists
 * @param snapshotId The version identifier for the current playlist. Can be supplied in other
 * requests to target a specific playlist version
 * @param tracks A collection containing a link (href) to the Web API endpoint where full details of
 * the playlist’s tracks can be retrieved, along with the total number of items in the playlist.
 * @param type The object type: “playlist”
 * @param uri The Spotify URI for the playlist.
 */
data class PlaylistObject(
    /* true if the owner allows other users to modify the playlist. */
    @Json(name = "collaborative") val collaborative: Boolean,
    @Json(name = "external_urls") val externalUrls: Any,
    /* A link to the Web API endpoint providing full details of the playlist. */
    @Json(name = "href") val href: String,
    /* The Spotify ID for the playlist. */
    @Json(name = "id") val id: String,
    /* Images for the playlist. The array may be empty or contain up to three images. The images are returned by size in descending order. See Working with Playlists. Note: If returned, the source URL for the image (url) is temporary and will expire in less than a day. */
    @Json(name = "images") val images: List<ImageObject>,
    /* The name of the playlist. */
    @Json(name = "name") val name: String,
    @Json(name = "owner") val owner: PublicUserObject,
    /* The playlist’s public/private status: true the playlist is public, false the playlist is private, null the playlist status is not relevant. For more about public/private status, see Working with Playlists */
    @Json(name = "public") val public: Boolean? = null,
    /* The version identifier for the current playlist. Can be supplied in other requests to target a specific playlist version */
    @Json(name = "snapshot_id") val snapshotId: String,
    /* A collection containing a link (href) to the Web API endpoint where full details of the playlist’s tracks can be retrieved, along with the total number of items in the playlist. */
    @Json(name = "tracks") val tracks: List<PlaylistTrackObject>,
    /* The object type: “playlist” */
    @Json(name = "type") val type: String,
    /* The Spotify URI for the playlist. */
    @Json(name = "uri") val uri: String
)
