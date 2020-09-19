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
 * @param seeds An array of recommendation seed objects.
 * @param tracks An array of track object (simplified) ordered according to the parameters supplied.
 */

data class RecommendationsResponseObject(
    /* An array of recommendation seed objects. */
    @Json(name = "seeds")
    val seeds: List<RecommendationSeedObject>? = null,
    /* An array of track object (simplified) ordered according to the parameters supplied. */
    @Json(name = "tracks")
    val tracks: List<SimplifiedTrackObject>? = null
)

