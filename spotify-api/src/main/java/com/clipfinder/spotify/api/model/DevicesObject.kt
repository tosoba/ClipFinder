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
 * @param devices A list of 0..n Device objects
 */

data class DevicesObject(
    /* A list of 0..n Device objects */
    @Json(name = "devices")
    val devices: List<DeviceObject>? = null
)
