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
 * @param name The name for the new playlist, for example \"Your Coolest Playlist\" . This name does not need to be unique; a user may have several playlists with the same name.
 * @param public Defaults to true . If true the playlist will be public, if false it will be private. To be able to create private playlists, the user must have granted the playlist-modify-private scope
 * @param collaborative Defaults to false . If true the playlist will be collaborative. Note that to create a collaborative playlist you must also set public to false . To create collaborative playlists you must have granted playlist-modify-private and playlist-modify-public scopes .
 * @param description value for playlist description as displayed in Spotify Clients and in the Web API.
 */

data class InlineObject11(
    /* The name for the new playlist, for example \"Your Coolest Playlist\" . This name does not need to be unique; a user may have several playlists with the same name. */
    @Json(name = "name")
    val name: String,
    /* Defaults to true . If true the playlist will be public, if false it will be private. To be able to create private playlists, the user must have granted the playlist-modify-private scope */
    @Json(name = "public")
    val public: Boolean? = null,
    /* Defaults to false . If true the playlist will be collaborative. Note that to create a collaborative playlist you must also set public to false . To create collaborative playlists you must have granted playlist-modify-private and playlist-modify-public scopes . */
    @Json(name = "collaborative")
    val collaborative: Boolean? = null,
    /* value for playlist description as displayed in Spotify Clients and in the Web API. */
    @Json(name = "description")
    val description: String? = null
)

