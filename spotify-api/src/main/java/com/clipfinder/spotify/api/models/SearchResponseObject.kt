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
 * @param album
 * @param artist
 * @param playlist
 * @param track
 * @param show
 * @param episode
 */

data class SearchResponseObject(
    @Json(name = "album")
    val album: InlineResponse2001? = null,
    @Json(name = "artist")
    val artist: SearchResponseObjectArtist? = null,
    @Json(name = "playlist")
    val playlist: InlineResponse2008? = null,
    @Json(name = "track")
    val track: InlineResponse200? = null,
    @Json(name = "show")
    val show: SearchResponseObjectShow? = null,
    @Json(name = "episode")
    val episode: InlineResponse2009? = null
)

