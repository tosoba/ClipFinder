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
 * @param context
 * @param currentlyPlayingType The object type of the currently playing item. Can be one of track, episode, ad or unknown.
 * @param isPlaying If something is currently playing, return true.
 * @param item The currently playing track or episode. Can be null.
 * @param progressMs Progress into the currently playing track or episode. Can be null.
 * @param timestamp Unix Millisecond Timestamp when data was fetched
 */

data class CurrentlyPlayingObject(
    @Json(name = "context")
    val context: ContextObject? = null,
    /* The object type of the currently playing item. Can be one of track, episode, ad or unknown. */
    @Json(name = "currently_playing_type")
    val currentlyPlayingType: String? = null,
    /* If something is currently playing, return true. */
    @Json(name = "is_playing")
    val isPlaying: Boolean? = null,
    /* The currently playing track or episode. Can be null. */
    @Json(name = "item")
    val item: TrackOrEpisodeObject? = null,
    /* Progress into the currently playing track or episode. Can be null. */
    @Json(name = "progress_ms")
    val progressMs: Int? = null,
    /* Unix Millisecond Timestamp when data was fetched */
    @Json(name = "timestamp")
    val timestamp: Int? = null
)

