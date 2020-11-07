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
package com.clipfinder.spotify.api.model

import com.squareup.moshi.Json

/**
 *
 * @param artists The artists who performed the track. Each artist object includes a link in href to more detailed information about the artist.
 * @param availableMarkets A list of the countries in which the track can be played, identified by their ISO 3166-1 alpha-2 code.
 * @param discNumber The disc number (usually 1 unless the album consists of more than one disc).
 * @param durationMs The track length in milliseconds.
 * @param explicit Whether or not the track has explicit lyrics ( true = yes it does; false = no it does not OR unknown).
 * @param externalUrls
 * @param href A link to the Web API endpoint providing full details of the track.
 * @param id The Spotify ID for the track.
 * @param isPlayable Part of the response when Track Relinking is applied. If true , the track is playable in the given market. Otherwise false.
 * @param linkedFrom
 * @param name The name of the track.
 * @param previewUrl A URL to a 30 second preview (MP3 format) of the track.
 * @param trackNumber The number of the track. If an album has several discs, the track number is the number on the specified disc.
 * @param type The object type: “track”.
 * @param uri The Spotify URI for the track.
 */

data class SimplifiedTrackObject(
    /* The artists who performed the track. Each artist object includes a link in href to more detailed information about the artist. */
    @Json(name = "artists")
    val artists: List<SimplifiedArtistObject>,
    /* A list of the countries in which the track can be played, identified by their ISO 3166-1 alpha-2 code. */
    @Json(name = "available_markets")
    val availableMarkets: List<String>? = null,
    /* The disc number (usually 1 unless the album consists of more than one disc). */
    @Json(name = "disc_number")
    val discNumber: Int,
    /* The track length in milliseconds. */
    @Json(name = "duration_ms")
    val durationMs: Int,
    /* Whether or not the track has explicit lyrics ( true = yes it does; false = no it does not OR unknown). */
    @Json(name = "explicit")
    val explicit: Boolean,
    @Json(name = "external_urls")
    val externalUrls: Any,
    /* A link to the Web API endpoint providing full details of the track. */
    @Json(name = "href")
    val href: String,
    /* The Spotify ID for the track. */
    @Json(name = "id")
    val id: String,
    /* Part of the response when Track Relinking is applied. If true , the track is playable in the given market. Otherwise false. */
    @Json(name = "is_playable")
    val isPlayable: Boolean? = null,
    @Json(name = "linked_from")
    val linkedFrom: LinkedTrackObject? = null,
    /* The name of the track. */
    @Json(name = "name")
    val name: String,
    /* A URL to a 30 second preview (MP3 format) of the track. */
    @Json(name = "preview_url")
    val previewUrl: String? = null,
    /* The number of the track. If an album has several discs, the track number is the number on the specified disc. */
    @Json(name = "track_number")
    val trackNumber: Int,
    /* The object type: “track”. */
    @Json(name = "type")
    val type: String,
    /* The Spotify URI for the track. */
    @Json(name = "uri")
    val uri: String
)

