package com.example.spotifyapi.models

import com.example.spotifyapi.util.match
import com.google.gson.annotations.SerializedName
import com.neovisionaries.i18n.CountryCode

/**
 * Simplified Album object that can be used to retrieve a full [Album]
 *
 * @property href A link to the Web API endpoint providing full details of the album.
 * @property id The Spotify ID for the album.
 * @property albumGroup Optional. The field is present when getting an artist’s items. Possible values
 * are “album”, “single”, “compilation”, “appears_on”. Compare to album_type this field represents relationship
 * between the artist and the album.
 * @property artists The artists of the album. Each artist object includes a link in href to more detailed information about the artist.
 * @property availableMarkets The markets in which the album is available: ISO 3166-1 alpha-2 country codes. Note
 * that an album is considered available in a market when at least 1 of its tracks is available in that market.
 * @property images The cover art for the album in various sizes, widest first.
 * @property name The name of the album. In case of an album takedown, the value may be an empty string.
 * @property type The object type: “album”
 * @property releaseDate The date the album was first released, for example 1981. Depending on the precision,
 * it might be shown as 1981-12 or 1981-12-15.
 * @property releaseDatePrecision The precision with which release_date value is known: year , month , or day.
 * @property albumType The type of the album: one of “album”, “single”, or “compilation”.
 * @property restrictions Part of the response when Track Relinking is applied, the original track is not available
 * in the given market, and Spotify did not have any tracks to relink it with. The track response will still contain
 * metadata for the original track, and a restrictions object containing the reason why the track is not available:
 * "restrictions" : {"reason" : "market"}
 */
data class SimpleAlbum(
    @SerializedName("album_type") private val _albumType: String,
    @SerializedName("available_markets") private val _availableMarkets: List<String> = listOf(),
    @SerializedName("external_urls") val externalUrls: Map<String, String>,
    @SerializedName("href") val href: String,
    val id: String,
    @SerializedName("uri") val uri: String,
    val artists: List<SimpleArtist>,
    val images: List<SpotifyImage>,
    val name: String,
    val type: String,
    val restrictions: Restrictions? = null,
    @SerializedName("release_date") val releaseDate: String,
    @SerializedName("release_date_precision") val releaseDatePrecision: String,
    @SerializedName("total_tracks") val totalTracks: Int? = null,
    @SerializedName("album_group") private val albumGroupString: String? = null
) {
    val availableMarkets get() = _availableMarkets.map { CountryCode.valueOf(it) }

    val albumType: AlbumResultType
        get() = _albumType.let { _ ->
            AlbumResultType.values().first { it.id.equals(_albumType, true) }
        }

    val albumGroup: AlbumResultType?
        get() = albumGroupString?.let { _ ->
            AlbumResultType.values().find { it.id == albumGroupString }
        }
}

/**
 * Album search type
 */
enum class AlbumResultType(internal val id: String) {
    ALBUM("album"),
    SINGLE("single"),
    COMPILATION("compilation"),
    APPEARS_ON("appears_on")
}

/**
 * Represents an Album on Spotify
 *
 * @property albumType The type of the album: one of "album" , "single" , or "compilation".
 * @property artists The artists of the album. Each artist object includes a link in href to more detailed
 * information about the artist.
 * @property availableMarkets The markets in which the album is available:
 * ISO 3166-1 alpha-2 country codes. Note that an album is considered
 * available in a market when at least 1 of its tracks is available in that market.
 * @property copyrights The copyright statements of the album.
 * @property externalIds Known external IDs for the album.
 * @property genres A list of the genres used to classify the album. For example: "Prog Rock" ,
 * "Post-Grunge". (If not yet classified, the array is empty.)
 * @property href A link to the Web API endpoint providing full details of the album.
 * @property id The Spotify ID for the album.
 * @property images The cover art for the album in various sizes, widest first.
 * @property label The label for the album.
 * @property name The name of the album. In case of an album takedown, the value may be an empty string.
 * @property popularity The popularity of the album. The value will be between 0 and 100, with 100 being the most
 * popular. The popularity is calculated from the popularity of the album’s individual tracks.
 * @property releaseDate The date the album was first released, for example 1981. Depending on the precision,
 * it might be shown as 1981-12 or 1981-12-15.
 * @property releaseDatePrecision The precision with which release_date value is known: year , month , or day.
 * @property tracks The tracks of the album.
 * @property type The object type: “album”
 * @property totalTracks the total amount of tracks in this album
 * @property restrictions Part of the response when Track Relinking is applied, the original track is not available
 * in the given market, and Spotify did not have any tracks to relink it with.
 * The track response will still contain metadata for the original track, and a
 * restrictions object containing the reason why the track is not available: "restrictions" : {"reason" : "market"}
 */
data class Album(
    @SerializedName("album_type") private val _albumType: String,
    @SerializedName("available_markets") private val _availableMarkets: List<String> = listOf(),
    @SerializedName("external_urls") val externalUrls: Map<String, String>,
    @SerializedName("external_ids") private val _externalIds: Map<String, String> = hashMapOf(),
    @SerializedName("href") val href: String,
    val id: String,
    @SerializedName("uri") val uri: String,

    val artists: List<SimpleArtist>,
    val copyrights: List<SpotifyCopyright>,
    val genres: List<String>,
    val images: List<SpotifyImage>,
    val label: String,
    val name: String,
    val popularity: Int,
    @SerializedName("release_date") val releaseDate: String,
    @SerializedName("release_date_precision") val releaseDatePrecision: String,
    val tracks: PagingObject<SimpleTrack>,
    val type: String,
    @SerializedName("total_tracks") val totalTracks: Int,
    val restrictions: Restrictions? = null
) {
    val availableMarkets get() = _availableMarkets.map { CountryCode.valueOf(it) }

    val externalIds get() = _externalIds.map { ExternalId(it.key, it.value) }

    val albumType: AlbumResultType get() = AlbumResultType.values().first { it.id == _albumType }
}

/**
 * Describes an album's copyright information
 *
 * @property text The copyright text for this album.
 * @property type The type of copyright: C = the copyright,
 * P = the sound recording (performance) copyright.
 */
data class SpotifyCopyright(
    @SerializedName("text") private val _text: String,
    @SerializedName("type") private val _type: String
) {
    val text
        get() = _text
            .removePrefix("(P)")
            .removePrefix("(C)")
            .trim()

    val type get() = CopyrightType.values().match(_type)!!
}

/**
 * Copyright statement type of an Album
 */
enum class CopyrightType(val identifier: String) : ResultEnum {
    COPYRIGHT("C"),
    SOUND_PERFORMANCE_COPYRIGHT("P");

    override fun retrieveIdentifier() = identifier
}
