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
import org.threeten.bp.OffsetDateTime

/**
 *
 * @param addedAt The date and time the album was saved Timestamps are returned in ISO 8601 format
 * as Coordinated Universal Time (UTC) with a zero offset: YYYY-MM-DDTHH:MM:SSZ. If the time is
 * imprecise (for example, the date/time of an album release), an additional field indicates the
 * precision; see for example, release_date in an album object.
 * @param album
 */
data class SavedAlbumObject(
    /* The date and time the album was saved Timestamps are returned in ISO 8601 format as Coordinated Universal Time (UTC) with a zero offset: YYYY-MM-DDTHH:MM:SSZ. If the time is imprecise (for example, the date/time of an album release), an additional field indicates the precision; see for example, release_date in an album object. */
    @Json(name = "added_at") val addedAt: OffsetDateTime,
    @Json(name = "album") val album: AlbumObject
)
