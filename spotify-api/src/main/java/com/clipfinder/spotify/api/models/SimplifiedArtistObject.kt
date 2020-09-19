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
 * @param externalUrls
 * @param href A link to the Web API endpoint providing full details of the artist.
 * @param id The Spotify ID for the artist.
 * @param name The name of the artist.
 * @param type The object type: \"artist\"
 * @param uri The Spotify URI for the artist.
 */

data class SimplifiedArtistObject(
    @Json(name = "external_urls")
    val externalUrls: Any? = null,
    /* A link to the Web API endpoint providing full details of the artist. */
    @Json(name = "href")
    val href: String? = null,
    /* The Spotify ID for the artist. */
    @Json(name = "id")
    val id: String? = null,
    /* The name of the artist. */
    @Json(name = "name")
    val name: String? = null,
    /* The object type: \"artist\" */
    @Json(name = "type")
    val type: String? = null,
    /* The Spotify URI for the artist. */
    @Json(name = "uri")
    val uri: String? = null
)

