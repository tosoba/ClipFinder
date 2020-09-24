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
 * @param fullyPlayed Whether or not the episode has been fully played by the user.
 * @param resumePositionMs The user’s most recent position in the episode in milliseconds.
 */

data class ResumePointObject(
    /* Whether or not the episode has been fully played by the user. */
    @Json(name = "fully_played")
    val fullyPlayed: Boolean? = null,
    /* The user’s most recent position in the episode in milliseconds. */
    @Json(name = "resume_position_ms")
    val resumePositionMs: Int? = null
)
