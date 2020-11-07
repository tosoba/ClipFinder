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

import com.clipfinder.core.spotify.model.ISpotifySimplifiedAlbum
import com.squareup.moshi.Json

/**
 *
 * @param albumType The type of the album: album, single, or compilation.
 * @param artists The artists of the album. Each artist object includes a link in href to more detailed information about the artist.
 * @param availableMarkets The markets in which the album is available: ISO 3166-1 alpha-2 country codes. Note that an album is considered available in a market when at least 1 of its tracks is available in that market.
 * @param copyrights The copyright statements of the album.
 * @param externalIds
 * @param externalUrls
 * @param genres A list of the genres used to classify the album. For example: “Prog Rock” , “Post-Grunge”. (If not yet classified, the array is empty.)
 * @param href A link to the Web API endpoint providing full details of the album.
 * @param id The Spotify ID for the album.
 * @param images The cover art for the album in various sizes, widest first.
 * @param label The label for the album.
 * @param name The name of the album. In case of an album takedown, the value may be an empty string.
 * @param popularity The popularity of the album. The value will be between 0 and 100, with 100 being the most popular. The popularity is calculated from the popularity of the album’s individual tracks.
 * @param releaseDate The date the album was first released, for example “1981-12-15”. Depending on the precision, it might be shown as “1981” or “1981-12”.
 * @param releaseDatePrecision The precision with which release_date value is known: “year” , “month” , or “day”.
 * @param tracks The tracks of the album.
 * @param type The object type: “album”
 * @param uri The Spotify URI for the album.
 */

data class AlbumObject(
    /* The type of the album: album, single, or compilation. */
    @Json(name = "album_type")
    override val albumType: String,
    /* The artists of the album. Each artist object includes a link in href to more detailed information about the artist. */
    @Json(name = "artists")
    override val artists: List<SimplifiedArtistObject>,
    /* The markets in which the album is available: ISO 3166-1 alpha-2 country codes. Note that an album is considered available in a market when at least 1 of its tracks is available in that market. */
    @Json(name = "available_markets")
    val availableMarkets: List<String>? = null,
    /* The copyright statements of the album. */
    @Json(name = "copyrights")
    val copyrights: List<CopyrightObject>,
    @Json(name = "external_ids")
    val externalIds: ExternalIdObject,
    @Json(name = "external_urls")
    val externalUrls: Any,
    /* A list of the genres used to classify the album. For example: “Prog Rock” , “Post-Grunge”. (If not yet classified, the array is empty.) */
    @Json(name = "genres")
    val genres: List<String>? = null,
    /* A link to the Web API endpoint providing full details of the album. */
    @Json(name = "href")
    override val href: String,
    /* The Spotify ID for the album. */
    @Json(name = "id")
    override val id: String,
    /* The cover art for the album in various sizes, widest first. */
    @Json(name = "images")
    override val images: List<ImageObject>,
    /* The label for the album. */
    @Json(name = "label")
    val label: String,
    /* The name of the album. In case of an album takedown, the value may be an empty string. */
    @Json(name = "name")
    override val name: String,
    /* The popularity of the album. The value will be between 0 and 100, with 100 being the most popular. The popularity is calculated from the popularity of the album’s individual tracks. */
    @Json(name = "popularity")
    val popularity: Int,
    /* The date the album was first released, for example “1981-12-15”. Depending on the precision, it might be shown as “1981” or “1981-12”. */
    @Json(name = "release_date")
    val releaseDate: String,
    /* The precision with which release_date value is known: “year” , “month” , or “day”. */
    @Json(name = "release_date_precision")
    val releaseDatePrecision: String,
    /* The tracks of the album. */
    @Json(name = "tracks")
    val tracks: SimplifiedTracksPagingObject,
    /* The object type: “album” */
    @Json(name = "type")
    val type: String,
    /* The Spotify URI for the album. */
    @Json(name = "uri")
    override val uri: String
) : ISpotifySimplifiedAlbum

