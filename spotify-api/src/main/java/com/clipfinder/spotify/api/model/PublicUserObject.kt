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
 * @param displayName The name displayed on the user’s profile. null if not available.
 * @param externalUrls
 * @param followers
 * @param href A link to the Web API endpoint for this user.
 * @param id The Spotify user ID for this user.
 * @param images The user’s profile image.
 * @param type The object type: “user”
 * @param uri The Spotify URI for this user.
 */

data class PublicUserObject(
    /* The name displayed on the user’s profile. null if not available. */
    @Json(name = "display_name")
    val displayName: String? = null,
    @Json(name = "external_urls")
    val externalUrls: Any,
    @Json(name = "followers")
    val followers: FollowersObject? = null,
    /* A link to the Web API endpoint for this user. */
    @Json(name = "href")
    val href: String,
    /* The Spotify user ID for this user. */
    @Json(name = "id")
    val id: String,
    /* The user’s profile image. */
    @Json(name = "images")
    val images: List<ImageObject>,
    /* The object type: “user” */
    @Json(name = "type")
    val type: String,
    /* The Spotify URI for this user. */
    @Json(name = "uri")
    val uri: String
)

