package com.clipfinder.spotify.api.model

import com.example.core.model.IPagingObject
import com.squareup.moshi.Json

data class ArtistsPagingObject(
    /* A link to the Web API endpoint returning the full result of the request */
    @Json(name = "href")
    val href: String,
    /* The requested data. */
    @Json(name = "items")
    override val items: List<ArtistObject>,
    /* The maximum number of items in the response (as set in the query or by default). */
    @Json(name = "limit")
    val limit: Int,
    /* URL to the next page of items. ( null if none) */
    @Json(name = "next")
    val next: String? = null,
    /* The offset of the items returned (as set in the query or by default) */
    @Json(name = "offset")
    override val offset: Int,
    /* URL to the previous page of items. ( null if none) */
    @Json(name = "previous")
    val previous: String? = null,
    /* The total number of items available to return. */
    @Json(name = "total")
    override val total: Int
) : IPagingObject<ArtistObject>
