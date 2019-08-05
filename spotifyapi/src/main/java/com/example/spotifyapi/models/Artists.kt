package com.example.spotifyapi.models

import com.squareup.moshi.Json

/**
 * Simplified Artist object that can be used to retrieve a full [Artist]
 *
 * @property href A link to the Web API endpoint providing full details of the artist.
 * @property id The Spotify ID for the artist.
 * @property name The name of the artist
 * @property type The object type: "artist"
 */
data class SimpleArtist(
        @Json(name = "external_urls") val externalUrls: Map<String, String>,
        @Json(name = "href") val href: String,
        val id: String,
        @Json(name = "uri") val uri: String,

        val name: String,
        val type: String
)

/**
 * Represents an Artist (distinct from a regular user) on Spotify
 *
 * @property followers Information about the followers of the artist.
 * @property genres A list of the genres the artist is associated with. For example: "Prog Rock" ,
 * "Post-Grunge". (If not yet classified, the array is empty.)
 * @property href A link to the Web API endpoint providing full details of the artist.
 * @property id The Spotify ID for the artist.
 * @property images Images of the artist in various sizes, widest first.
 * @property name The name of the artist
 * @property popularity The popularity of the artist. The value will be between 0 and 100, with 100 being the most
 * popular. The artist’s popularity is calculated from the popularity of all the artist’s tracks.
 * @property type The object type: "artist"
 */
data class Artist(
        @Json(name = "external_urls") val externalUrls: Map<String, String>,
        @Json(name = "href") val href: String,
        val id: String,
        @Json(name = "uri") val uri: String,

        val followers: Followers,
        val genres: List<String>,
        val images: List<SpotifyImage>,
        val name: String,
        val popularity: Int,
        val type: String
)

internal data class ArtistList(val artists: List<Artist>)