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
 * @param afterFilteringSize The number of tracks available after min_* and max_* filters have been applied.
 * @param afterRelinkingSize The number of tracks available after relinking for regional availability.
 * @param href A link to the full track or artist data for this seed. For tracks this will be a link to a Track Object. For artists a link to an Artist Object. For genre seeds, this value will be null.
 * @param id The id used to select this seed. This will be the same as the string used in the seed_artists, seed_tracks or seed_genres parameter.
 * @param initialPoolSize The number of recommended tracks available for this seed.
 * @param type The entity type of this seed. One of artist, track or genre.
 */

data class RecommendationSeedObject(
    /* The number of tracks available after min_* and max_* filters have been applied. */
    @Json(name = "afterFilteringSize")
    val afterFilteringSize: Int? = null,
    /* The number of tracks available after relinking for regional availability. */
    @Json(name = "afterRelinkingSize")
    val afterRelinkingSize: Int? = null,
    /* A link to the full track or artist data for this seed. For tracks this will be a link to a Track Object. For artists a link to an Artist Object. For genre seeds, this value will be null. */
    @Json(name = "href")
    val href: String? = null,
    /* The id used to select this seed. This will be the same as the string used in the seed_artists, seed_tracks or seed_genres parameter. */
    @Json(name = "id")
    val id: String? = null,
    /* The number of recommended tracks available for this seed. */
    @Json(name = "initialPoolSize")
    val initialPoolSize: Int? = null,
    /* The entity type of this seed. One of artist, track or genre. */
    @Json(name = "type")
    val type: String? = null
)
