/**
 * Spotify Web API
 * No description provided (generated by Openapi Generator https://github.com/openapitools/openapi-generator)
 *
 * The version of the OpenAPI document: 0.0.1
 *
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */
package com.clipfinder.spotify.api.models

import com.squareup.moshi.Json

/**
 *
 * @param addedAt The date and time the track or episode was added. Note that some very old playlists may return null in this field.
 * @param addedBy
 * @param isLocal Whether this track or episode is a local file or not.
 * @param track Information about the track or episode.
 */

data class PlaylistTrackObject(
    /* The date and time the track or episode was added. Note that some very old playlists may return null in this field. */
    @Json(name = "added_at")
    val addedAt: java.time.OffsetDateTime? = null,
    @Json(name = "added_by")
    val addedBy: PublicUserObject? = null,
    /* Whether this track or episode is a local file or not. */
    @Json(name = "is_local")
    val isLocal: Boolean? = null,
    /* Information about the track or episode. */
    @Json(name = "track")
    val track: TrackOrEpisodeObject? = null
)

