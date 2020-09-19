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
 * @param cursors
 * @param href A link to the Web API endpoint returning the full result of the request.
 * @param items The requested data.
 * @param limit The maximum number of items in the response (as set in the query or by default).
 * @param next URL to the next page of items. ( null if none)
 * @param total The total number of items available to return.
 */

data class FollowingArtistsObjectArtists(
    @Json(name = "cursors")
    val cursors: CursorObject? = null,
    /* A link to the Web API endpoint returning the full result of the request. */
    @Json(name = "href")
    val href: String? = null,
    /* The requested data. */
    @Json(name = "items")
    val items: List<ArtistObject>? = null,
    /* The maximum number of items in the response (as set in the query or by default). */
    @Json(name = "limit")
    val limit: Int? = null,
    /* URL to the next page of items. ( null if none) */
    @Json(name = "next")
    val next: String? = null,
    /* The total number of items available to return. */
    @Json(name = "total")
    val total: Int? = null
)

