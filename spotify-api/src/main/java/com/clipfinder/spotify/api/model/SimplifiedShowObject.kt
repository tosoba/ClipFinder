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
 * @param availableMarkets A list of the countries in which the show can be played, identified by
 * their ISO 3166-1 alpha-2 code.
 * @param copyrights The copyright statements of the show.
 * @param description A description of the show.
 * @param explicit Whether or not the show has explicit content (true = yes it does; false = no it
 * does not OR unknown).
 * @param externalUrls
 * @param href A link to the Web API endpoint providing full details of the show.
 * @param id The Spotify ID for the show.
 * @param images The cover art for the show in various sizes, widest first.
 * @param isExternallyHosted True if all of the show’s episodes are hosted outside of Spotify’s CDN.
 * This field might be null in some cases.
 * @param languages A list of the languages used in the show, identified by their ISO 639 code.
 * @param mediaType The media type of the show.
 * @param name The name of the episode.
 * @param publisher The publisher of the show.
 * @param type The object type: “show”.
 * @param uri The Spotify URI for the show.
 */
data class SimplifiedShowObject(
    /* A list of the countries in which the show can be played, identified by their ISO 3166-1 alpha-2 code. */
    @Json(name = "available_markets") val availableMarkets: List<String>? = null,
    /* The copyright statements of the show. */
    @Json(name = "copyrights") val copyrights: List<CopyrightObject>? = null,
    /* A description of the show. */
    @Json(name = "description") val description: String? = null,
    /* Whether or not the show has explicit content (true = yes it does; false = no it does not OR unknown). */
    @Json(name = "explicit") val explicit: Boolean? = null,
    @Json(name = "external_urls") val externalUrls: Any? = null,
    /* A link to the Web API endpoint providing full details of the show. */
    @Json(name = "href") val href: String? = null,
    /* The Spotify ID for the show. */
    @Json(name = "id") val id: String? = null,
    /* The cover art for the show in various sizes, widest first. */
    @Json(name = "images") val images: List<ImageObject>? = null,
    /* True if all of the show’s episodes are hosted outside of Spotify’s CDN. This field might be null in some cases. */
    @Json(name = "is_externally_hosted") val isExternallyHosted: Boolean? = null,
    /* A list of the languages used in the show, identified by their ISO 639 code. */
    @Json(name = "languages") val languages: List<String>? = null,
    /* The media type of the show. */
    @Json(name = "media_type") val mediaType: String? = null,
    /* The name of the episode. */
    @Json(name = "name") val name: String? = null,
    /* The publisher of the show. */
    @Json(name = "publisher") val publisher: String? = null,
    /* The object type: “show”. */
    @Json(name = "type") val type: String? = null,
    /* The Spotify URI for the show. */
    @Json(name = "uri") val uri: String? = null
)
