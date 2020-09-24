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
 * @param snapshotId The snapshot_id can be used to identify your playlist version in future requests.
 */

data class SnapshotIdObject(
    /* The snapshot_id can be used to identify your playlist version in future requests. */
    @Json(name = "snapshot_id")
    val snapshotId: String? = null
)
