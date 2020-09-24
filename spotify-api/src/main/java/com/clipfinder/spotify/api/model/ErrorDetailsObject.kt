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
 * @param status The HTTP status code that is also returned in the response header.
 * @param message A short description of the cause of the error.
 */

data class ErrorDetailsObject(
    /* The HTTP status code that is also returned in the response header. */
    @Json(name = "status")
    val status: Int? = null,
    /* A short description of the cause of the error. */
    @Json(name = "message")
    val message: String? = null
)
