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

import com.squareup.moshi.Json

/**
 *
 * @param album
 * @param artist
 * @param playlist
 * @param track
 * @param show
 * @param episode
 */
data class SearchResponseObject(
    @Json(name = "album") val album: SimplifiedAlbumsPagingObject? = null,
    @Json(name = "artist") val artist: SearchResponseObjectArtist? = null,
    @Json(name = "playlist") val playlist: SimplifiedPlaylistsPagingObject? = null,
    @Json(name = "track") val track: TracksPagingObject? = null,
    @Json(name = "show") val show: SearchResponseObjectShow? = null,
    @Json(name = "episode") val episode: SimplifiedEpisodesPagingObject? = null
)
