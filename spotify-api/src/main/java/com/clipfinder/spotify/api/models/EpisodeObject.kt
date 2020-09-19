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
 * @param audioPreviewUrl A URL to a 30 second preview (MP3 format) of the episode. null if not available.
 * @param description A description of the episode.
 * @param durationMs The episode length in milliseconds.
 * @param explicit Whether or not the episode has explicit content (true = yes it does; false = no it does not OR unknown).
 * @param externalUrls
 * @param href A link to the Web API endpoint providing full details of the episode.
 * @param id The Spotify ID for the episode.
 * @param images The cover art for the episode in various sizes, widest first.
 * @param isExternallyHosted True if the episode is hosted outside of Spotify’s CDN.
 * @param isPlayable True if the episode is playable in the given market. Otherwise false.
 * @param language Note: This field is deprecated and might be removed in the future. Please use the languages field instead. The language used in the episode, identified by a ISO 639 code.
 * @param languages A list of the languages used in the episode, identified by their ISO 639 code.
 * @param name The name of the episode.
 * @param releaseDate The date the episode was first released, for example \"1981-12-15\". Depending on the precision, it might be shown as \"1981\" or \"1981-12\".
 * @param releaseDatePrecision The precision with which release_date value is known: \"year\", \"month\", or \"day\".
 * @param resumePoint
 * @param show
 * @param type The object type: “episode”.
 * @param uri The Spotify URI for the episode.
 */

data class EpisodeObject(
    /* A URL to a 30 second preview (MP3 format) of the episode. null if not available. */
    @Json(name = "audio_preview_url")
    val audioPreviewUrl: String? = null,
    /* A description of the episode. */
    @Json(name = "description")
    val description: String? = null,
    /* The episode length in milliseconds. */
    @Json(name = "duration_ms")
    val durationMs: Int? = null,
    /* Whether or not the episode has explicit content (true = yes it does; false = no it does not OR unknown). */
    @Json(name = "explicit")
    val explicit: Boolean? = null,
    @Json(name = "external_urls")
    val externalUrls: Any? = null,
    /* A link to the Web API endpoint providing full details of the episode. */
    @Json(name = "href")
    val href: String? = null,
    /* The Spotify ID for the episode. */
    @Json(name = "id")
    val id: String? = null,
    /* The cover art for the episode in various sizes, widest first. */
    @Json(name = "images")
    val images: List<ImageObject>? = null,
    /* True if the episode is hosted outside of Spotify’s CDN. */
    @Json(name = "is_externally_hosted")
    val isExternallyHosted: Boolean? = null,
    /* True if the episode is playable in the given market. Otherwise false. */
    @Json(name = "is_playable")
    val isPlayable: Boolean? = null,
    /* Note: This field is deprecated and might be removed in the future. Please use the languages field instead. The language used in the episode, identified by a ISO 639 code. */
    @Json(name = "language")
    val language: String? = null,
    /* A list of the languages used in the episode, identified by their ISO 639 code. */
    @Json(name = "languages")
    val languages: List<String>? = null,
    /* The name of the episode. */
    @Json(name = "name")
    val name: String? = null,
    /* The date the episode was first released, for example \"1981-12-15\". Depending on the precision, it might be shown as \"1981\" or \"1981-12\". */
    @Json(name = "release_date")
    val releaseDate: String? = null,
    /* The precision with which release_date value is known: \"year\", \"month\", or \"day\". */
    @Json(name = "release_date_precision")
    val releaseDatePrecision: String? = null,
    @Json(name = "resume_point")
    val resumePoint: ResumePointObject? = null,
    @Json(name = "show")
    val show: SimplifiedShowObject? = null,
    /* The object type: “episode”. */
    @Json(name = "type")
    val type: String? = null,
    /* The Spotify URI for the episode. */
    @Json(name = "uri")
    val uri: String? = null
)

