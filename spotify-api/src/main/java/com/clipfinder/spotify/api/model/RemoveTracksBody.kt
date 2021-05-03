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
 * @param tracks An array of objects containing Spotify URIs of the tracks or episodes to remove.
 * For example: { \"tracks\": [{ \"uri\": \"spotify:track:4iV5W9uYEdYUVa79Axb7Rh\" },{ \"uri\":
 * \"spotify:track:1301WleyT98MSxVHPZCA6M\" }] }. A maximum of 100 objects can be sent at once.
 * @param snapshotId The playlist’s snapshot ID against which you want to make the changes. The API
 * will validate that the specified items exist and in the specified positions and make the changes,
 * even if more recent changes have been made to the playlist.
 */
data class RemoveTracksBody(
    /* An array of objects containing Spotify URIs of the tracks or episodes to remove. For example: { \"tracks\": [{ \"uri\": \"spotify:track:4iV5W9uYEdYUVa79Axb7Rh\" },{ \"uri\": \"spotify:track:1301WleyT98MSxVHPZCA6M\" }] }. A maximum of 100 objects can be sent at once. */
    @Json(name = "tracks") val tracks: List<String>,
    /* The playlist’s snapshot ID against which you want to make the changes. The API will validate that the specified items exist and in the specified positions and make the changes, even if more recent changes have been made to the playlist. */
    @Json(name = "snapshot_id") val snapshotId: String? = null
)
