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

import com.clipfinder.core.spotify.model.ISpotifyCategory
import com.squareup.moshi.Json

/**
 *
 * @param href A link to the Web API endpoint returning full details of the category.
 * @param icons The category icon, in various sizes.
 * @param id The Spotify category ID of the category.
 * @param name The name of the category.
 */
data class CategoryObject(
    /* A link to the Web API endpoint returning full details of the category. */
    @Json(name = "href") override val href: String,
    /* The category icon, in various sizes. */
    @Json(name = "icons") override val icons: List<ImageObject>,
    /* The Spotify category ID of the category. */
    @Json(name = "id") override val id: String,
    /* The name of the category. */
    @Json(name = "name") override val name: String
) : ISpotifyCategory
