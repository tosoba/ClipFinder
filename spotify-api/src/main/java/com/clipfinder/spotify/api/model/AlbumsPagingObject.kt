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
 * @param href A link to the Web API endpoint returning the full result of the request
 * @param items The requested data.
 * @param limit The maximum number of items in the response (as set in the query or by default).
 * @param next URL to the next page of items. ( null if none)
 * @param offset The offset of the items returned (as set in the query or by default)
 * @param previous URL to the previous page of items. ( null if none)
 * @param total The total number of items available to return.
 */

data class AlbumsPagingObject(
    /* A link to the Web API endpoint returning the full result of the request */
    @Json(name = "href")
    val href: String,
    /* The requested data. */
    @Json(name = "items")
    val items: List<SavedAlbumObject>,
    /* The maximum number of items in the response (as set in the query or by default). */
    @Json(name = "limit")
    val limit: Int,
    /* URL to the next page of items. ( null if none) */
    @Json(name = "next")
    val next: String? = null,
    /* The offset of the items returned (as set in the query or by default) */
    @Json(name = "offset")
    val offset: Int,
    /* URL to the previous page of items. ( null if none) */
    @Json(name = "previous")
    val previous: String? = null,
    /* The total number of items available to return. */
    @Json(name = "total")
    val total: Int
)

